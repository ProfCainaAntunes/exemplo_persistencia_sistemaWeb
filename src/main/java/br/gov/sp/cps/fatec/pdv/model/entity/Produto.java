package br.gov.sp.cps.fatec.pdv.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Entidade de Domínio representando um Produto no subsistema de Ponto de Venda
 * (PDV).
 * <p>
 * <b>Conceito Pedagógico (Mapeamento Objeto-Relacional - ORM):</b>
 * A JPA resolve o <i>Impedance Mismatch</i> (descompasso de impedância) entre o
 * paradigma
 * orientado a objetos (classes, herança, encapsulamento) e o modelo relacional
 * (tabelas, chaves
 * primárias, chaves estrangeiras).
 * <ul>
 * <li>{@link Entity}: Informa ao provedor JPA (Hibernate) que instâncias desta
 * classe devem
 * ser mapeadas para uma tabela relacional.</li>
 * <li>{@link Table}: Define o nome e as restrições da tabela física.</li>
 * <li>{@link Id} e {@link GeneratedValue}: Define a chave primária com
 * estratégia autoincremento
 * (IDENTITY), delegando ao mecanismo de sequence/identity do SGBD (H2).</li>
 * <li>{@link Column}: Mapeia propriedades para colunas com restrições DDL (not
 * null, unique, precisão).</li>
 * </ul>
 *
 * @author Cainã Antunes Silva
 */
@Entity
@Table(name = "produtos")
@NamedQueries({
        @NamedQuery(name = "Produto.listarTodos", query = "SELECT p FROM Produto p ORDER BY p.nome ASC"),
        @NamedQuery(name = "Produto.buscarPorCodigoBarras", query = "SELECT p FROM Produto p WHERE p.codigoBarras = :codigoBarras")
})
public class Produto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "codigo_barras", unique = true, nullable = false, length = 50)
    private String codigoBarras;

    @Column(name = "nome", nullable = false, length = 120)
    private String nome;

    @Column(name = "preco", nullable = false, precision = 12, scale = 2)
    private BigDecimal preco;

    @Column(name = "quantidade_estoque", nullable = false)
    private Integer quantidadeEstoque;

    /**
     * Construtor padrão sem argumentos (exigência da especificação JPA/Hibernate
     * para instanciação via reflexão).
     */
    public Produto() {
    }

    /**
     * Construtor de conveniência para instanciação de novos produtos antes da
     * persistência (sem id).
     *
     * @param codigoBarras      Código de barras único do produto.
     * @param nome              Nome descritivo do produto.
     * @param preco             Preço unitário de venda (deve ser positivo).
     * @param quantidadeEstoque Quantidade disponível em estoque (não-negativo).
     */
    public Produto(String codigoBarras, String nome, BigDecimal preco, Integer quantidadeEstoque) {
        this.codigoBarras = codigoBarras;
        this.nome = nome;
        this.preco = preco;
        this.quantidadeEstoque = quantidadeEstoque;
    }

    /**
     * Construtor completo incluindo o identificador.
     *
     * @param id                Identificador único gerado pelo banco de dados.
     * @param codigoBarras      Código de barras único do produto.
     * @param nome              Nome descritivo do produto.
     * @param preco             Preço unitário de venda.
     * @param quantidadeEstoque Quantidade em estoque.
     */
    public Produto(Long id, String codigoBarras, String nome, BigDecimal preco, Integer quantidadeEstoque) {
        this(codigoBarras, nome, preco, quantidadeEstoque);
        this.id = id;
    }

    // ==========================================
    // GETTERS E SETTERS
    // ==========================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public Integer getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setQuantidadeEstoque(Integer quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
    }

    // ==========================================
    // EQUALS, HASHCODE E TOSTRING
    // ==========================================

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Produto produto = (Produto) o;
        // Se ambos têm id, compara por id; caso contrário, compara pela chave natural
        // (codigoBarras)
        if (this.id != null && produto.id != null) {
            return Objects.equals(this.id, produto.id);
        }
        return Objects.equals(this.codigoBarras, produto.codigoBarras);
    }

    @Override
    public int hashCode() {
        return (id != null) ? Objects.hash(id) : Objects.hash(codigoBarras);
    }

    @Override
    public String toString() {
        return "Produto{" +
                "id=" + id +
                ", codigoBarras='" + codigoBarras + '\'' +
                ", nome='" + nome + '\'' +
                ", preco=" + preco +
                ", quantidadeEstoque=" + quantidadeEstoque +
                '}';
    }
}
