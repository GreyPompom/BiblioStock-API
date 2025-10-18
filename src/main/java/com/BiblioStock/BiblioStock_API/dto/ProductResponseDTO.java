package com.BiblioStock.BiblioStock_API.dto;

import com.BiblioStock.BiblioStock_API.model.Product;
import java.util.Set;
import java.math.BigDecimal;
import com.BiblioStock.BiblioStock_API.model.Author;
import java.util.stream.Collectors;


public record ProductResponseDTO(
        Long id,
        String name,
        String productType,
        BigDecimal price,
        String unit,
        BigDecimal stockQty,
        BigDecimal minQty,
        BigDecimal maxQty,
        String publisher,
        String isbn,
        CategoryResponseDTO category,
        Set<AuthorResponseDTO> authors
) {
    public static ProductResponseDTO fromEntity(Product p) {
        return new ProductResponseDTO(
                p.getId(),
                p.getName(),
                p.getProductType(),
                p.getPrice(),
                p.getUnit(),
                p.getStockQty(),
                p.getMinQty(),
                p.getMaxQty(),
                p.getPublisher(),
                p.getIsbn(),
                p.getCategory() != null ? CategoryResponseDTO.fromEntity(p.getCategory()) : null,
                p.getAuthors() != null
                        ? p.getAuthors().stream()
                            .map(AuthorResponseDTO::fromEntity)
                            .collect(Collectors.toSet())
                        : Set.of()
        );
    }
}
