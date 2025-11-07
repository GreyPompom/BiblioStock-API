package com.BiblioStock.BiblioStock_API.service;

import com.BiblioStock.BiblioStock_API.dto.ProductResponseDTO;
import com.BiblioStock.BiblioStock_API.model.Product;
import com.BiblioStock.BiblioStock_API.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductAuthorReportService {

    private final ProductRepository productRepository;

    public ProductAuthorReportService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
public List<ProductResponseDTO> getProductsPerAuthor(Long authorId) {
    List<Product> products = productRepository.findByAuthorId(authorId);

    return products.stream()
            .map(ProductResponseDTO::fromEntity)
            .collect(Collectors.toList());
    }
}
    

