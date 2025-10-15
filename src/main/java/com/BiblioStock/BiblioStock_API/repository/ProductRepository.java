package com.BiblioStock.BiblioStock_API.repository;

import com.BiblioStock.BiblioStock_API.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
