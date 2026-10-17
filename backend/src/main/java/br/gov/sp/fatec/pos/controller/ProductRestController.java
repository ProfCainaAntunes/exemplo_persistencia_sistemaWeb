package br.gov.sp.fatec.pos.controller;

import br.gov.sp.fatec.pos.dto.ProductRequestDTO;
import br.gov.sp.fatec.pos.dto.ProductResponseDTO;
import br.gov.sp.fatec.pos.entity.Product;
import br.gov.sp.fatec.pos.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador RESTful (Controller) para gerenciamento de Produtos.
 * <p>
 * <b>Conceito Pedagógico (Padrão MVC & Arquitetura RESTful):</b>
 * <ul>
 *   <li>{@link RestController}: Combina {@link Controller} e {@link ResponseBody}, serializando automaticamente
 *       as respostas para o formato JSON.</li>
 *   <li>{@link RequestMapping}: Define a rota base da API (/api/products).</li>
 *   <li>{@link Valid}: Aciona a validação declarativa do Bean Validation (Jakarta) no DTO de entrada antes
 *       de alcançar a camada de serviço.</li>
 *   <li>Utiliza os verbos HTTP canônicos (GET, POST, PUT, DELETE) e códigos de status HTTP semânticos
 *       (200 OK, 201 Created, 204 No Content, 400 Bad Request, 404 Not Found).</li>
 * </ul>
 *
 * @author Cainã Antunes Silva
 */
@RestController
@RequestMapping("/api/products")
@Tag(name = "Products", description = "Endpoints de gerenciamento e persistência de produtos do catálogo PDV")
@CrossOrigin(origins = "*")
public class ProductRestController {

    private final ProductService productService;

    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Retorna a lista completa de produtos cadastrados.
     *
     * @return Lista com status 200 (OK).
     */
    @Operation(summary = "Listar todos os produtos", description = "Retorna todos os produtos persistidos no banco de dados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista recuperada com sucesso")
    })
    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> findAll() {
        List<ProductResponseDTO> list = productService.findAll().stream()
                .map(ProductResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    /**
     * Busca um produto específico pelo seu ID.
     *
     * @param id Identificador do produto.
     * @return Dados do produto (200 OK) ou 404 (Not Found).
     */
    @Operation(summary = "Buscar produto por ID", description = "Retorna os detalhes de um produto dado seu identificador único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto localizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produto não localizado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> findById(@PathVariable Long id) {
        return productService.findById(id)
                .map(p -> ResponseEntity.ok(ProductResponseDTO.fromEntity(p)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Cadastra um novo produto no catálogo.
     *
     * @param request DTO com as informações do produto a cadastrar.
     * @return Produto cadastrado com status 201 (Created) e cabeçalho Location.
     */
    @Operation(summary = "Criar novo produto", description = "Persiste um novo produto após validação de integridade e unicidade de código de barras.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Produto criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou código de barras duplicado")
    })
    @PostMapping
    public ResponseEntity<ProductResponseDTO> create(@Valid @RequestBody ProductRequestDTO request) {
        Product entity = new Product(
                request.getBarcode(),
                request.getName(),
                request.getPrice(),
                request.getStockQuantity()
        );
        Product saved = productService.save(entity);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();

        return ResponseEntity.created(location).body(ProductResponseDTO.fromEntity(saved));
    }

    /**
     * Atualiza os dados de um produto existente.
     *
     * @param id Identificador do produto a atualizar.
     * @param request DTO com as alterações desejadas.
     * @return Produto atualizado com status 200 (OK).
     */
    @Operation(summary = "Atualizar produto", description = "Atualiza as informações de um produto existente pelo identificador.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> update(@PathVariable Long id, @Valid @RequestBody ProductRequestDTO request) {
        if (!productService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Product entity = new Product(
                id,
                request.getBarcode(),
                request.getName(),
                request.getPrice(),
                request.getStockQuantity()
        );
        Product updated = productService.save(entity);

        return ResponseEntity.ok(ProductResponseDTO.fromEntity(updated));
    }

    /**
     * Exclui um produto cadastrado.
     *
     * @param id Identificador do produto.
     * @return Status 204 (No Content) após exclusão.
     */
    @Operation(summary = "Excluir produto", description = "Remove permanentemente um produto do banco de dados relacional.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Produto removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produto não localizado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        if (!productService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
