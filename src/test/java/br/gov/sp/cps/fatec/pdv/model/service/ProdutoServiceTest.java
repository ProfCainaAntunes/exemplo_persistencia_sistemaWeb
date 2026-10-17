package br.gov.sp.cps.fatec.pdv.model.service;

import br.gov.sp.cps.fatec.pdv.model.entity.Produto;
import br.gov.sp.cps.fatec.pdv.model.observer.ProdutoObserver;
import br.gov.sp.cps.fatec.pdv.model.repository.ProdutoRepository;
import br.gov.sp.cps.fatec.pdv.model.repository.ProdutoRepositoryImpl;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes Unitários das Regras de Negócio da Camada de Serviço e do Padrão Observer.
 */
class ProdutoServiceTest {

    private static EntityManagerFactory emf;
    private ProdutoRepository repository;
    private ProdutoService service;

    @BeforeAll
    static void setUpAll() {
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
        service = new ProdutoService(repository);
    }

    @Test
    @DisplayName("Deve rejeitar produto com preço negativo ou zero")
    void deveRejeitarPrecoInvalido() {
        Produto pZero = new Produto("111", "Item Zero", BigDecimal.ZERO, 10);
        assertThrows(IllegalArgumentException.class, () -> service.salvar(pZero));

        Produto pNegativo = new Produto("112", "Item Negativo", new BigDecimal("-5.00"), 10);
        assertThrows(IllegalArgumentException.class, () -> service.salvar(pNegativo));
    }

    @Test
    @DisplayName("Deve rejeitar produto com quantidade em estoque negativa")
    void deveRejeitarEstoqueNegativo() {
        Produto p = new Produto("113", "Item Estoque Inválido", new BigDecimal("10.00"), -1);
        assertThrows(IllegalArgumentException.class, () -> service.salvar(p));
    }

    @Test
    @DisplayName("Deve rejeitar produto com código de barras duplicado")
    void deveRejeitarCodigoBarrasDuplicado() {
        Produto p1 = new Produto("9999", "Produto Original", new BigDecimal("15.00"), 5);
        service.salvar(p1);

        Produto p2 = new Produto("9999", "Produto Clonado", new BigDecimal("20.00"), 8);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.salvar(p2));
        assertTrue(ex.getMessage().contains("Já existe um produto cadastrado com o código de barras"));
    }

    @Test
    @DisplayName("Deve cadastrar produto válido e notificar observador")
    void deveSalvarENotificarObservadores() throws InterruptedException {
        AtomicBoolean observadorNotificado = new AtomicBoolean(false);

        ProdutoObserver observer = produtos -> {
            observadorNotificado.set(true);
        };
        service.addObserver(observer);

        Produto p = new Produto("8888", "Produto Observer Test", new BigDecimal("12.50"), 10);
        service.salvar(p);

        // Aguarda despacho na EDT do Swing
        Thread.sleep(300);
        assertTrue(observadorNotificado.get(), "O observador deve ser notificado após salvar o produto");
    }
}
