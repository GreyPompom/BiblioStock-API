package com.BiblioStock.BiblioStock_API.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.BiblioStock.BiblioStock_API.model.Product;
import com.BiblioStock.BiblioStock_API.model.Category;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
     boolean existsByCategory(Category category);
    boolean existsByIsbn(String isbn);
    boolean existsBySku(String sku);
    Optional<Product> findByNameIgnoreCase(String name);
    // Find products by category
    List<Product> findByCategory_Id(Long categoryId);

    @Query(value = "SELECT id, name, product_count FROM vw_products_per_category", nativeQuery = true)
    List<Object[]> findProductsPerCategoryRaw();

    @Query(value = "SELECT id, name, product_count " +
                   "FROM vw_products_per_category " +
                   "WHERE id = :categoryId", nativeQuery = true)
    List<Object[]> findProductsPerCategoryByCategoryId(Long categoryId);
}