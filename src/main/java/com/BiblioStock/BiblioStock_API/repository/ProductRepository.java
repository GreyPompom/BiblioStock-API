package com.BiblioStock.BiblioStock_API.repository;

import com.BiblioStock.BiblioStock_API.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByIsbn(String isbn);
     boolean existsBySku(String sku);
    Optional<Product> findByNameIgnoreCase(String name);
}