package br.gov.sp.fatec.pos.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Entidade de Domínio representando um Produto (Product) no subsistema de Ponto de Venda (PDV).
 * <p>
 * <b>Conceito Pedagógico (Mapeamento Objeto-Relacional - ORM com Hibernate e Spring Data JPA):</b>
 * A JPA resolve o <i>Impedance Mismatch</i> (descompasso de impedância) entre o paradigma orientado
 * a objetos (classes, herança, encapsulamento) e o modelo relacional (tabelas, tuplas, restrições).
 * <ul>
 *   <li>{@link Entity}: Informa ao provedor JPA (Hibernate) que instâncias desta classe são persistíveis.</li>
 *   <li>{@link Table}: Mapeia a entidade para a tabela relacional 'products'.</li>
 *   <li>{@link Id} e {@link GeneratedValue}: Define a chave primária autoincrementada (IDENTITY).</li>
 *   <li>{@link Column}: Define as restrições físicas da coluna no banco de dados.</li>
 * </ul>
 *
 * @author Cainã Antunes Silva
 */
@Entity
@Table(name = "products")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "O código de barras é obrigatório.")
    @Column(name = "barcode", nullable = false, unique = true, length = 50)
    private String barcode;

    @NotBlank(message = "O nome do produto é obrigatório.")
    @Column(name = "name", nullable = false, length = 120)
    private String name;

    @NotNull(message = "O preço é obrigatório.")
    @DecimalMin(value = "0.01", message = "O preço unitário deve ser maior que zero.")
    @Column(name = "price", nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @NotNull(message = "A quantidade em estoque é obrigatória.")
    @Min(value = 0, message = "A quantidade em estoque não pode ser negativa.")
    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity;

    /**
     * Construtor padrão sem argumentos (exigência da especificação JPA/Hibernate
     * para instanciação reflexiva).
     */
    public Product() {
    }

    /**
     * Construtor de conveniência para instanciação de novos produtos antes da persistência (sem id).
     *
     * @param barcode       Código de barras único do produto.
     * @param name          Nome descritivo do produto.
     * @param price         Preço unitário de venda (maior que zero).
     * @param stockQuantity Quantidade disponível em estoque (não-negativo).
     */
    public Product(String barcode, String name, BigDecimal price, Integer stockQuantity) {
        this.barcode = barcode;
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    /**
     * Construtor completo incluindo o identificador primário.
     *
     * @param id            Identificador gerado pelo banco.
     * @param barcode       Código de barras único do produto.
     * @param name          Nome descritivo do produto.
     * @param price         Preço unitário de venda.
     * @param stockQuantity Quantidade em estoque.
     */
    public Product(Long id, String barcode, String name, BigDecimal price, Integer stockQuantity) {
        this(barcode, name, price, stockQuantity);
        this.id = id;
    }

    // ==========================================
    // GETTERS E SETTERS (PADRONIZADOS EM INGLÊS)
    // ==========================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    // ==========================================
    // EQUALS, HASHCODE E TOSTRING
    // ==========================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        if (this.id != null && product.id != null) {
            return Objects.equals(this.id, product.id);
        }
        return Objects.equals(this.barcode, product.barcode);
    }

    @Override
    public int hashCode() {
        return (id != null) ? Objects.hash(id) : Objects.hash(barcode);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", barcode='" + barcode + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", stockQuantity=" + stockQuantity +
                '}';
    }
}
