package br.gov.sp.fatec.pos.service;

import br.gov.sp.fatec.pos.entity.Product;
import br.gov.sp.fatec.pos.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes de Integração e de Regras de Negócio para o {@link ProductService}.
 * <p>
 * <b>Conceito Pedagógico (Estratégia de Testes Automatizados em Camadas):</b>
 * Valida a integridade das transações do Spring Data JPA, regras de validação do domínio
 * e persistência relacional sobre a base embutida H2 em memória.
 *
 * @author Cainã Antunes Silva
 */
@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ProductServiceTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve salvar produto válido com sucesso e gerar identificador")
    void deveSalvarProdutoValido() {
        Product product = new Product("7891112223334", "Monitor Gamer 144Hz", new BigDecimal("1299.90"), 8);
        Product salvo = productService.save(product);

        assertNotNull(salvo.getId(), "O ID do produto deve ser gerado pelo banco de dados.");
        assertEquals("7891112223334", salvo.getBarcode());
        assertEquals("Monitor Gamer 144Hz", salvo.getName());
    }

    @Test
    @DisplayName("Deve rejeitar produto com preço zero ou negativo")
    void deveRejeitarPrecoInvalido() {
        Product pZero = new Product("111", "Item Preço Zero", BigDecimal.ZERO, 10);
        IllegalArgumentException exZero = assertThrows(IllegalArgumentException.class, () -> productService.save(pZero));
        assertTrue(exZero.getMessage().contains("maior que zero"));

        Product pNegativo = new Product("112", "Item Preço Negativo", new BigDecimal("-5.00"), 10);
        IllegalArgumentException exNegativo = assertThrows(IllegalArgumentException.class, () -> productService.save(pNegativo));
        assertTrue(exNegativo.getMessage().contains("maior que zero"));
    }

    @Test
    @DisplayName("Deve rejeitar produto com quantidade em estoque negativa")
    void deveRejeitarEstoqueNegativo() {
        Product p = new Product("113", "Item Estoque Negativo", new BigDecimal("10.00"), -1);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> productService.save(p));
        assertTrue(ex.getMessage().contains("não pode ser negativa"));
    }

    @Test
    @DisplayName("Deve rejeitar produto com código de barras duplicado")
    void deveRejeitarCodigoBarrasDuplicado() {
        Product p1 = new Product("9999", "Produto Original", new BigDecimal("15.00"), 5);
        productService.save(p1);

        Product p2 = new Product("9999", "Produto Clonado", new BigDecimal("20.00"), 8);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> productService.save(p2));
        assertTrue(ex.getMessage().contains("Já existe um produto com o código de barras informado"));
    }

    @Test
    @DisplayName("Deve buscar produtos cadastrados por listagem e por ID")
    void deveBuscarProdutos() {
        Product p1 = productService.save(new Product("101", "Mouse Óptico", new BigDecimal("50.00"), 10));
        Product p2 = productService.save(new Product("102", "Teclado USB", new BigDecimal("80.00"), 15));

        List<Product> todos = productService.findAll();
        assertEquals(2, todos.size());

        Optional<Product> encontrado = productService.findById(p1.getId());
        assertTrue(encontrado.isPresent());
        assertEquals("Mouse Óptico", encontrado.get().getName());
    }

    @Test
    @DisplayName("Deve excluir produto por ID com sucesso e lançar exceção se inexistente")
    void deveExcluirProduto() {
        Product p = productService.save(new Product("103", "Headset", new BigDecimal("150.00"), 3));
        Long id = p.getId();

        productService.deleteById(id);
        assertFalse(productService.findById(id).isPresent());

        assertThrows(IllegalArgumentException.class, () -> productService.deleteById(id));
    }
}
