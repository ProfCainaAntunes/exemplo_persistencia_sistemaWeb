package br.gov.sp.fatec.pos.service;

import br.gov.sp.fatec.pos.entity.Product;
import br.gov.sp.fatec.pos.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Camada de Serviço de Negócio (Service Layer).
 * <p>
 * <b>Conceito Pedagógico (Gerenciamento Declarativo de Transações e Regras de Negócio):</b>
 * <ul>
 *   <li><b>Inversão de Controle e Injeção de Dependências (IoC/DI):</b> Recebe o {@link ProductRepository} via construtor.</li>
 *   <li><b>Controle Transacional ACID:</b> A anotação {@link Transactional} gerencia automaticamente o início, commit
 *       ou rollback de transações do banco relacional via Spring Transaction Manager.</li>
 *   <li><b>Validação de Regras de Domínio:</b> Garante unicidade do código de barras, valores monetários válidos
 *       e consistência de estoque antes de invocar a persistência.</li>
 * </ul>
 *
 * @author Cainã Antunes Silva
 */
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = Objects.requireNonNull(productRepository, "ProductRepository não pode ser nulo.");
    }

    /**
     * Retorna todos os produtos cadastrados ordenados.
     *
     * @return Lista completa de produtos.
     */
    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    /**
     * Localiza um produto pelo seu identificador primário.
     *
     * @param id Identificador do produto.
     * @return {@link Optional} contendo a entidade se encontrada.
     */
    @Transactional(readOnly = true)
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    /**
     * Localiza um produto pelo seu código de barras único.
     *
     * @param barcode Código de barras.
     * @return {@link Optional} contendo a entidade se encontrada.
     */
    @Transactional(readOnly = true)
    public Optional<Product> findByBarcode(String barcode) {
        return productRepository.findByBarcode(barcode);
    }

    /**
     * Salva ou atualiza um produto após aplicar as validações de regras de negócio.
     *
     * @param product Entidade a ser persistida.
     * @return Produto persistido com chave primária preenchida.
     */
    @Transactional
    public Product save(Product product) {
        validateBusinessRules(product);

        // Validação da regra de negócio: unicidade de código de barras
        Optional<Product> existing = productRepository.findByBarcode(product.getBarcode());
        if (existing.isPresent()) {
            boolean isSameProduct = product.getId() != null && existing.get().getId().equals(product.getId());
            if (!isSameProduct) {
                throw new IllegalArgumentException("Já existe um produto com o código de barras informado: " + product.getBarcode());
            }
        }

        return productRepository.save(product);
    }

    /**
     * Remove um produto pelo seu identificador primário.
     *
     * @param id Identificador do produto a ser excluído.
     */
    @Transactional
    public void deleteById(Long id) {
        if (!productRepository.existsById(id)) {
            throw new IllegalArgumentException("Produto não localizado para exclusão com ID: " + id);
        }
        productRepository.deleteById(id);
    }

    /**
     * Validações de integridade e regras de negócio da entidade.
     */
    private void validateBusinessRules(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Dados do produto não podem ser nulos.");
        }
        if (product.getBarcode() == null || product.getBarcode().trim().isEmpty()) {
            throw new IllegalArgumentException("O Código de Barras é obrigatório.");
        }
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("O Nome do Produto é obrigatório.");
        }
        if (product.getPrice() == null) {
            throw new IllegalArgumentException("O Preço do Produto é obrigatório.");
        }
        if (product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O Preço do Produto deve ser estritamente positivo (maior que zero).");
        }
        if (product.getStockQuantity() == null) {
            throw new IllegalArgumentException("A Quantidade em Estoque é obrigatória.");
        }
        if (product.getStockQuantity() < 0) {
            throw new IllegalArgumentException("A Quantidade em Estoque não pode ser negativa.");
        }
    }
}
