package br.gov.sp.fatec.pos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * DTO (Data Transfer Object) de Requisição para Cadastro e Atualização de Produto.
 * <p>
 * <b>Conceito Pedagógico (Padrão DTO e Validação de Entrada):</b>
 * Desacopla o contrato da API pública da entidade interna de persistência do banco,
 * garantindo que apenas os dados permitidos sejam recebidos e validados pela camada web.
 *
 * @author Cainã Antunes Silva
 */
@Schema(description = "Payload de entrada para criação ou atualização de produto")
public class ProductRequestDTO {

    @Schema(description = "Código de barras único do produto", example = "7891234567890", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "O código de barras é obrigatório.")
    private String barcode;

    @Schema(description = "Nome descritivo do produto", example = "Teclado Mecânico RGB", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "O nome do produto é obrigatório.")
    private String name;

    @Schema(description = "Preço unitário do produto", example = "249.90", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "O preço é obrigatório.")
    @DecimalMin(value = "0.01", message = "O preço unitário deve ser maior que zero.")
    private BigDecimal price;

    @Schema(description = "Quantidade em estoque disponível", example = "15", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "A quantidade em estoque é obrigatória.")
    @Min(value = 0, message = "A quantidade em estoque não pode ser negativa.")
    private Integer stockQuantity;

    public ProductRequestDTO() {
    }

    public ProductRequestDTO(String barcode, String name, BigDecimal price, Integer stockQuantity) {
        this.barcode = barcode;
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
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
