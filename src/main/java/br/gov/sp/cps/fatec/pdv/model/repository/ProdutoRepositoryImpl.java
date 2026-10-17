package br.gov.sp.cps.fatec.pdv.model.repository;

import br.gov.sp.cps.fatec.pdv.model.entity.Produto;
import br.gov.sp.cps.fatec.pdv.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Implementação do {@link ProdutoRepository} utilizando a JPA (Java Persistence
 * API) com Hibernate.
 * <p>
 * <b>Conceito Pedagógico (Gerenciamento Explícito de Transações e
 * Recursos):</b>
 * <ul>
 * <li>Cada operação que modifica dados (DML - Insert, Update, Delete) requer a
 * demarcação de uma transação
 * atômica (ACID): {@code em.getTransaction().begin()}, {@code commit()} e em
 * caso de falha {@code rollback()}.</li>
 * <li>O fechamento do {@link EntityManager} em bloco {@code finally} é crucial
 * para evitar vazamento
 * de conexões com o banco de dados (Connection Leak).</li>
 * <li>Injeção de Dependências (IoC): A classe permite receber um
 * {@link EntityManagerFactory}
 * customizado no construtor, possibilitando testes com banco em memória sem
 * alterar a lógica.</li>
 * </ul>
 *
 * @author Cainã Antunes Silva
 */
public class ProdutoRepositoryImpl implements ProdutoRepository {

    private final EntityManagerFactory emf;

    /**
     * Construtor padrão que obtém o {@link EntityManagerFactory} singleton via
     * {@link JPAUtil}.
     */
    public ProdutoRepositoryImpl() {
        this(JPAUtil.getEntityManagerFactory());
    }

    /**
     * Construtor com Injeção de Dependência (IoC) da fábrica de conexões.
     *
     * @param emf Fábrica {@link EntityManagerFactory} a ser utilizada para criar
     *            conexões.
     */
    public ProdutoRepositoryImpl(EntityManagerFactory emf) {
        this.emf = Objects.requireNonNull(emf, "EntityManagerFactory não pode ser nulo.");
    }

    @Override
    public Produto salvar(Produto produto) {
        Objects.requireNonNull(produto, "Produto não pode ser nulo.");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(produto);
            tx.commit();
            return produto;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Erro ao persistir produto: " + e.getMessage(), e);
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public Produto atualizar(Produto produto) {
        Objects.requireNonNull(produto, "Produto não pode ser nulo.");
        if (produto.getId() == null) {
            throw new IllegalArgumentException("Produto a ser atualizado deve possuir um ID.");
        }
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Produto mesclado = em.merge(produto);
            tx.commit();
            return mesclado;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Erro ao atualizar produto: " + e.getMessage(), e);
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public void excluir(Long id) {
        Objects.requireNonNull(id, "ID do produto não pode ser nulo para exclusão.");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Produto produto = em.find(Produto.class, id);
            if (produto == null) {
                throw new IllegalArgumentException("Produto não encontrado para o ID: " + id);
            }
            em.remove(produto);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Erro ao excluir produto ID " + id + ": " + e.getMessage(), e);
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public Optional<Produto> buscarPorId(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        EntityManager em = emf.createEntityManager();
        try {
            Produto produto = em.find(Produto.class, id);
            return Optional.ofNullable(produto);
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public Optional<Produto> buscarPorCodigoBarras(String codigoBarras) {
        if (codigoBarras == null || codigoBarras.trim().isEmpty()) {
            return Optional.empty();
        }
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Produto> query = em.createNamedQuery("Produto.buscarPorCodigoBarras", Produto.class);
            query.setParameter("codigoBarras", codigoBarras.trim());
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public List<Produto> listarTodos() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Produto> query = em.createNamedQuery("Produto.listarTodos", Produto.class);
            return query.getResultList();
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }
}
