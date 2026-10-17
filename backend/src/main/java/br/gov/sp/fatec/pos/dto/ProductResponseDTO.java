package br.gov.sp.fatec.pos.dto;

import br.gov.sp.fatec.pos.entity.Product;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

/**
 * DTO (Data Transfer Object) de Resposta para representação de Produto na API RESTful.
 * <p>
 * <b>Conceito Pedagógico (Encapsulamento e Contrato de API):</b>
 * Permite que a estrutura de retorno da API evolua independentemente da modelagem física
 * das tabelas do banco de dados, protegendo informações internas.
 *
 * @author Cainã Antunes Silva
 */
@Schema(description = "Representação pública de produto retornada pela API REST")
public class ProductResponseDTO {

    @Schema(description = "Identificador único gerado automaticamente pelo banco", example = "1")
    private Long id;

    @Schema(description = "Código de barras único do produto", example = "7891234567890")
    private String barcode;

    @Schema(description = "Nome descritivo do produto", example = "Teclado Mecânico RGB")
    private String name;

    @Schema(description = "Preço unitário de venda", example = "249.90")
    private BigDecimal price;

    @Schema(description = "Quantidade disponível em estoque", example = "15")
    private Integer stockQuantity;

    public ProductResponseDTO() {
    }

    public ProductResponseDTO(Long id, String barcode, String name, BigDecimal price, Integer stockQuantity) {
        this.id = id;
        this.barcode = barcode;
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    public static ProductResponseDTO fromEntity(Product product) {
        if (product == null) {
            return null;
        }
        return new ProductResponseDTO(
                product.getId(),
                product.getBarcode(),
                product.getName(),
                product.getPrice(),
                product.getStockQuantity()
        );
    }

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
}
