package com.BiblioStock.BiblioStock_API.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.BiblioStock.BiblioStock_API.dto.ProductsPerCategoryDTO;
import com.BiblioStock.BiblioStock_API.service.ProductService;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*")
public class ReportsController {

    private final ProductService productService;

    public ReportsController(ProductService service) {
        this.productService = service;
    }

    @GetMapping("/products-per-category")
    public ResponseEntity<List<ProductsPerCategoryDTO>> getProductsPerCategory() {
        return ResponseEntity.ok(productService.getProductsPerCategory());
    }

    @GetMapping("/products-per-category/{categoryId}")
    public ResponseEntity<List<ProductsPerCategoryDTO>> getProductsPerCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(productService.getProductsPerCategoryByCategoryId(categoryId));
    }

}
