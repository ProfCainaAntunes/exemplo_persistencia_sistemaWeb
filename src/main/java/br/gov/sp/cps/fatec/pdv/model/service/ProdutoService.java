package br.gov.sp.cps.fatec.pdv.model.service;

import br.gov.sp.cps.fatec.pdv.model.entity.Produto;
import br.gov.sp.cps.fatec.pdv.model.observer.ProdutoObserver;
import br.gov.sp.cps.fatec.pdv.model.repository.ProdutoRepository;

import javax.swing.SwingUtilities;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Camada de Serviço de Negócio (Service Layer / Subject no Padrão Observer).
 * <p>
 * <b>Conceito Pedagógico (Regras de Negócio, IoC e Thread-Safety):</b>
 * <ul>
 * <li><b>Validação de Domínio:</b> Aplica validações estritas exigidas pelas
 * regras de negócio
 * (código de barras único e preenchido, nome obrigatório, preço estritamente
 * positivo,
 * estoque não-negativo).</li>
 * <li><b>Inversão de Dependência (DIP):</b> Depende exclusivamente da abstração
 * {@link ProdutoRepository}.</li>
 * <li><b>Padrão Observer (Subject):</b> Mantém a lista de ouvintes registrados
 * e dispara
 * notificações reativas após mutações de dados bem-sucedidas.</li>
 * <li><b>Thread Safety com Swing (EDT):</b> Assegura que o método
 * {@link ProdutoObserver#onProdutosChanged(List)}
 * seja sempre despachado na <i>Event Dispatch Thread</i> do Swing via
 * {@link SwingUtilities#invokeLater(Runnable)},
 * evitando condições de corrida (Race Conditions) e travamentos de UI.</li>
 * </ul>
 *
 * @author Cainã Antunes Silva
 */
public class ProdutoService {

    private final ProdutoRepository repository;
    private final List<ProdutoObserver> observers;

    /**
     * Construtor com Injeção de Dependências da interface de repositório.
     *
     * @param repository Abstração do repositório de produtos.
     */
    public ProdutoService(ProdutoRepository repository) {
        this.repository = Objects.requireNonNull(repository, "ProdutoRepository não pode ser nulo.");
        this.observers = new ArrayList<>();
    }

    // ==========================================
    // GERENCIAMENTO DE OBSERVERS (GOF PATTERN)
    // ==========================================

    /**
     * Registra um novo observador para receber notificações de mudanças de dados.
     *
     * @param observer Implementação de {@link ProdutoObserver}.
     */
    public synchronized void addObserver(ProdutoObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    /**
     * Remove um observador previamente registrado.
     *
     * @param observer Observador a ser removido.
     */
    public synchronized void removeObserver(ProdutoObserver observer) {
        observers.remove(observer);
    }

    /**
     * Notifica todos os observadores cadastrados com a lista de produtos
     * atualizada.
     * Garante o disparo seguro na Event Dispatch Thread (EDT) do Swing.
     */
    public void notificarObservadores() {
        List<Produto> listaAtualizada = repository.listarTodos();
        List<ProdutoObserver> copiaObservers;
        synchronized (this) {
            copiaObservers = new ArrayList<>(observers);
        }

        // Despacho seguro na Thread de Eventos do Swing (EDT)
        SwingUtilities.invokeLater(() -> {
            for (ProdutoObserver observer : copiaObservers) {
                try {
                    observer.onProdutosChanged(Collections.unmodifiableList(listaAtualizada));
                } catch (Exception e) {
                    System.err.println("Erro ao notificar observador: " + e.getMessage());
                }
            }
        });
    }

    // ==========================================
    // REGRAS DE NEGÓCIO E PERSISTÊNCIA
    // ==========================================

    /**
     * Cadastra um novo produto após validação completa de negócio.
     *
     * @param produto Entidade a ser cadastrada.
     * @return Produto salvo com ID atribuído.
     */
    public Produto salvar(Produto produto) {
        validarRegrasProduto(produto);

        // Validação de Unicidade do Código de Barras
        Optional<Produto> existente = repository.buscarPorCodigoBarras(produto.getCodigoBarras());
        if (existente.isPresent()) {
            throw new IllegalArgumentException(
                    "Já existe um produto cadastrado com o código de barras informado: " + produto.getCodigoBarras());
        }

        Produto salvo = repository.salvar(produto);
        notificarObservadores();
        return salvo;
    }

    /**
     * Atualiza um produto existente após validações de negócio.
     *
     * @param produto Entidade contendo as alterações.
     * @return Produto atualizado.
     */
    public Produto atualizar(Produto produto) {
        if (produto.getId() == null) {
            throw new IllegalArgumentException("O produto deve possuir um ID para ser atualizado.");
        }
        validarRegrasProduto(produto);

        // Validação de Código de Barras duplicado para outro produto diferente
        Optional<Produto> existente = repository.buscarPorCodigoBarras(produto.getCodigoBarras());
        if (existente.isPresent() && !existente.get().getId().equals(produto.getId())) {
            throw new IllegalArgumentException(
                    "O código de barras '" + produto.getCodigoBarras() +
                            "' já pertence a outro produto cadastrado (#" + existente.get().getId() + " - "
                            + existente.get().getNome() + ").");
        }

        Produto atualizado = repository.atualizar(produto);
        notificarObservadores();
        return atualizado;
    }

    /**
     * Exclui um produto pelo seu identificador e notifica os observadores.
     *
     * @param id Identificador do produto a ser removido.
     */
    public void excluir(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID inválido para exclusão.");
        }
        repository.excluir(id);
        notificarObservadores();
    }

    /**
     * Busca um produto pelo ID.
     *
     * @param id Identificador.
     * @return {@link Optional} contendo o produto.
     */
    public Optional<Produto> buscarPorId(Long id) {
        return repository.buscarPorId(id);
    }

    /**
     * Retorna a listagem atual de todos os produtos cadastrados.
     *
     * @return Lista de produtos.
     */
    public List<Produto> listarTodos() {
        return repository.listarTodos();
    }

    // ==========================================
    // VALIDAÇÕES INTERNAS DO DOMÍNIO
    // ==========================================

    private void validarRegrasProduto(Produto produto) {
        if (produto == null) {
            throw new IllegalArgumentException("Dados do produto não podem ser nulos.");
        }

        if (produto.getCodigoBarras() == null || produto.getCodigoBarras().trim().isEmpty()) {
            throw new IllegalArgumentException("O Código de Barras é obrigatório.");
        }

        if (produto.getNome() == null || produto.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("O Nome do Produto é obrigatório.");
        }

        if (produto.getPreco() == null) {
            throw new IllegalArgumentException("O Preço do Produto é obrigatório.");
        }

        if (produto.getPreco().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O Preço do Produto deve ser estritamente positivo (maior que zero).");
        }

        if (produto.getQuantidadeEstoque() == null) {
            throw new IllegalArgumentException("A Quantidade em Estoque é obrigatória.");
        }

        if (produto.getQuantidadeEstoque() < 0) {
            throw new IllegalArgumentException("A Quantidade em Estoque não pode ser negativa.");
        }
    }
}
