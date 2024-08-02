package com.example.e_commerce.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.e_commerce.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query("SELECT p FROM Product p")
    List<Product> findAllProducts(Pageable pageable);

    /*************
     * 
     * @param categoryId
     * @return
     */
    List<Product> findByCategoryId(Integer categoryId);

    /*************
     * 
     * @param searchKy
     * @return
     */
    @Query("""
                SELECT p FROM Product p WHERE
                    (
                    lower(p.name) LIKE lower(concat('%', :searchKy, '%'))
                    OR lower(p.shortDescription) LIKE lower(concat('%', :searchKy, '%'))
                    OR lower(p.longDescription) LIKE lower(concat('%', :searchKy, '%'))
                    )
            """)
    List<Product> findBySearchKey(String searchKy, Pageable pageable);

}
