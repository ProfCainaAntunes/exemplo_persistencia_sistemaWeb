package br.gov.sp.cps.fatec.pdv.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Utilitário de gerenciamento do ciclo de vida da JPA (Java Persistence API).
 * <p>
 * <b>Conceito Pedagógico (Padrão Singleton / Factory):</b>
 * <ul>
 * <li>{@link EntityManagerFactory}: Representa a fábrica de conexões e
 * metadados ORM.
 * É um objeto de custo computacional pesado (heavyweight), inicializado apenas
 * uma vez
 * durante o ciclo de vida da aplicação (Thread-Safe).</li>
 * <li>{@link EntityManager}: Representa a unidade de trabalho (Unit of Work) e
 * contexto
 * de persistência. É um objeto leve (lightweight), porém <b>NÃO</b> é
 * Thread-Safe. Cada
 * operação ou transação deve obter seu próprio {@code EntityManager} e fechá-lo
 * após o uso.</li>
 * </ul>
 * 
 * @author Cainã Antunes Silva
 */
public final class JPAUtil {

    private static final String DEFAULT_PERSISTENCE_UNIT = "pdv-pu";
    private static EntityManagerFactory emf;

    // Construtor privado para evitar instanciação (Padrão Utility Class)
    private JPAUtil() {
        throw new UnsupportedOperationException("Classe utilitária não deve ser instanciada.");
    }

    /**
     * Retorna a instância única de {@link EntityManagerFactory}.
     * Aplica o padrão Singleton com inicialização sob demanda (Lazy Loading).
     *
     * @return {@link EntityManagerFactory} ativo da aplicação.
     */
    public static synchronized EntityManagerFactory getEntityManagerFactory() {
        if (emf == null || !emf.isOpen()) {
            emf = Persistence.createEntityManagerFactory(DEFAULT_PERSISTENCE_UNIT);
        }
        return emf;
    }

    /**
     * Inicializa a fábrica com uma unidade de persistência customizada (ex.: para
     * testes integrados).
     *
     * @param persistenceUnitName Nome da unidade de persistência configurada no
     *                            persistence.xml.
     */
    public static synchronized void init(String persistenceUnitName) {
        close();
        emf = Persistence.createEntityManagerFactory(persistenceUnitName);
    }

    /**
     * Cria e retorna um novo {@link EntityManager} para operações de persistência.
     *
     * @return novo {@link EntityManager}.
     */
    public static EntityManager getEntityManager() {
        return getEntityManagerFactory().createEntityManager();
    }

    /**
     * Encerra a fábrica de conexões e libera todos os recursos de banco de dados.
     * Deve ser invocado no encerramento da aplicação Swing
     * (WindowListener/ShutdownHook).
     */
    public static synchronized void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
            emf = null;
        }
    }
}
