package br.gov.sp.fatec.pos.repository;

import br.gov.sp.fatec.pos.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório de dados desacoplado para a entidade {@link Product}.
 * <p>
 * <b>Conceito Pedagógico (Padrão Repository & Spring Data JPA):</b>
 * Ao estender {@link JpaRepository}, o Spring Data gera dinamicamente em tempo de execução
 * a implementação concreta com todas as operações CRUD (save, findById, findAll, deleteById),
 * paginação e controle transacional, eliminando código boilerplate e acoplamento direto com o
 * {@code EntityManager}.
 *
 * @author Cainã Antunes Silva
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Consulta derivada criada pelo mecanismo de convenção de nomes do Spring Data JPA.
     * Utilizada para verificação de unicidade da chave natural (código de barras).
     *
     * @param barcode Código de barras único.
     * @return {@link Optional} contendo o produto caso localizado.
     */
    Optional<Product> findByBarcode(String barcode);
}
