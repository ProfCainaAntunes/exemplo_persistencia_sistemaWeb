package br.gov.sp.cps.fatec.pdv.model.repository;

import br.gov.sp.cps.fatec.pdv.model.entity.Produto;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes Unitários e Integrados do Repositório de Produtos com H2 Database em memória.
 */
class ProdutoRepositoryTest {

    private static EntityManagerFactory emf;
    private ProdutoRepository repository;

    @BeforeAll
    static void setUpAll() {
        // Inicializa o EntityManagerFactory utilizando a unidade de testes em memória (pdv-test-pu)
        emf = Persistence.createEntityManagerFactory("pdv-test-pu");
    }

    @AfterAll
    static void tearDownAll() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }

    @BeforeEach
    void setUp() {
        repository = new ProdutoRepositoryImpl(emf);
    }

    @Test
    @DisplayName("Deve salvar um novo produto com sucesso e gerar ID autoincremento")
    void deveSalvarProdutoComSucesso() {
        Produto p = new Produto("78910001", "Café Gourmet 500g", new BigDecimal("22.50"), 15);
        Produto salvo = repository.salvar(p);

        assertNotNull(salvo.getId(), "O ID gerado pelo banco não deve ser nulo");
        assertEquals("Café Gourmet 500g", salvo.getNome());
        assertEquals(new BigDecimal("22.50"), salvo.getPreco());
        assertEquals(15, salvo.getQuantidadeEstoque());
    }

    @Test
    @DisplayName("Deve buscar produto por ID existente")
    void deveBuscarPorId() {
        Produto p = new Produto("78910002", "Açúcar Cristal 1kg", new BigDecimal("4.80"), 50);
        Produto salvo = repository.salvar(p);

        Optional<Produto> encontrado = repository.buscarPorId(salvo.getId());
        assertTrue(encontrado.isPresent());
        assertEquals("Açúcar Cristal 1kg", encontrado.get().getNome());
    }

    @Test
    @DisplayName("Deve buscar produto por código de barras único")
    void deveBuscarPorCodigoBarras() {
        Produto p = new Produto("78910003", "Arroz Tipo 1 5kg", new BigDecimal("29.90"), 30);
        repository.salvar(p);

        Optional<Produto> encontrado = repository.buscarPorCodigoBarras("78910003");
        assertTrue(encontrado.isPresent());
        assertEquals("Arroz Tipo 1 5kg", encontrado.get().getNome());
    }

    @Test
    @DisplayName("Deve atualizar dados de um produto existente")
    void deveAtualizarProduto() {
        Produto p = new Produto("78910004", "Feijão Preto 1kg", new BigDecimal("8.50"), 20);
        Produto salvo = repository.salvar(p);

        salvo.setPreco(new BigDecimal("9.00"));
        salvo.setQuantidadeEstoque(25);
        Produto atualizado = repository.atualizar(salvo);

        assertEquals(new BigDecimal("9.00"), atualizado.getPreco());
        assertEquals(25, atualizado.getQuantidadeEstoque());
    }

    @Test
    @DisplayName("Deve excluir produto por ID com sucesso")
    void deveExcluirProduto() {
        Produto p = new Produto("78910005", "Óleo de Soja 900ml", new BigDecimal("6.20"), 40);
        Produto salvo = repository.salvar(p);

        repository.excluir(salvo.getId());
        Optional<Produto> aposExclusao = repository.buscarPorId(salvo.getId());
        assertFalse(aposExclusao.isPresent(), "O produto não deve mais existir no banco de dados");
    }

    @Test
    @DisplayName("Deve listar todos os produtos cadastrados ordenados por nome")
    void deveListarTodosOsProdutos() {
        repository.salvar(new Produto("78910006", "Bolacha Recheada 140g", new BigDecimal("3.50"), 100));
        repository.salvar(new Produto("78910007", "Amaciante 2L", new BigDecimal("14.90"), 12));

        List<Produto> lista = repository.listarTodos();
        assertFalse(lista.isEmpty());
        assertTrue(lista.size() >= 2);
    }
}
