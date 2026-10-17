package br.gov.sp.cps.fatec.pdv.model.repository;

import br.gov.sp.cps.fatec.pdv.model.entity.Produto;

import java.util.List;
import java.util.Optional;

/**
 * Interface do Padrão de Projeto Repository (GoF / Martin Fowler) para a
 * entidade {@link Produto}.
 * <p>
 * <b>Conceito Pedagógico (Padrão Repository & Inversão de Dependência -
 * DIP):</b>
 * <ul>
 * <li>O padrão Repository abstrai a camada de acesso a dados (DAL), simulando
 * uma coleção
 * em memória de objetos de domínio.</li>
 * <li>Permite que as camadas superiores (Controller e Service) dependam de uma
 * <b>abstração</b>
 * (esta interface), e não de uma implementação concreta acoplada à tecnologia
 * de banco
 * (JPA/Hibernate, JDBC puro, NoSQL).</li>
 * <li>Facilita testes unitários utilizando mocks/stubs e promove o Princípio da
 * Responsabilidade
 * Única (SRP - SOLID).</li>
 * </ul>
 *
 * @author Cainã Antunes Silva
 */
public interface ProdutoRepository {

    /**
     * Persiste uma nova entidade {@link Produto} no banco de dados.
     *
     * @param produto Objeto a ser persistido (com id nulo).
     * @return O produto gerenciado com o ID gerado pelo banco.
     * @throws RuntimeException caso ocorra falha na transação de persistência.
     */
    Produto salvar(Produto produto);

    /**
     * Atualiza o estado de uma entidade {@link Produto} já existente.
     *
     * @param produto Objeto contendo os dados atualizados (deve possuir id válido).
     * @return A entidade gerenciada e sincronizada.
     * @throws RuntimeException caso ocorra falha na atualização.
     */
    Produto atualizar(Produto produto);

    /**
     * Remove uma entidade do banco de dados pelo seu identificador primário.
     *
     * @param id Identificador único do produto a ser excluído.
     * @throws RuntimeException caso o produto não seja encontrado ou ocorra erro
     *                          transacional.
     */
    void excluir(Long id);

    /**
     * Recupera um produto através do seu identificador único.
     *
     * @param id Chave primária do produto.
     * @return {@link Optional} contendo o produto encontrado, ou vazio se não
     *         existir.
     */
    Optional<Produto> buscarPorId(Long id);

    /**
     * Recupera um produto pelo seu código de barras único.
     *
     * @param codigoBarras Código de barras a ser consultado.
     * @return {@link Optional} contendo o produto encontrado, ou vazio se não
     *         existir.
     */
    Optional<Produto> buscarPorCodigoBarras(String codigoBarras);

    /**
     * Retorna a listagem completa de todos os produtos cadastrados.
     *
     * @return Lista contendo todos os produtos, ordenada alfabeticamente pelo nome.
     */
    List<Produto> listarTodos();
}
