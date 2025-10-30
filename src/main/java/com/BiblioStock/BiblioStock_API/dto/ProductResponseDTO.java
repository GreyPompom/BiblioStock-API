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
        String sku,
        CategoryResponseDTO category,
        Set<AuthorResponseDTO> authors,
        BigDecimal priceWithPercent
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
                p.getSku(),
                p.getCategory() != null ? CategoryResponseDTO.fromEntity(p.getCategory()) : null,
                p.getAuthors() != null
                ? p.getAuthors().stream()
                        .map(AuthorResponseDTO::fromEntity)
                        .collect(Collectors.toSet())
                : Set.of(),
                p.getPriceWithPercent()
        );
    }

    public Product toEntity() {
        Product product = new Product();
        product.setId(this.id);
        product.setName(this.name);
        product.setProductType(this.productType);
        product.setPrice(this.price);
        product.setUnit(this.unit);
        product.setStockQty(this.stockQty);
        product.setMinQty(this.minQty);
        product.setMaxQty(this.maxQty);
        product.setPublisher(this.publisher);
        product.setIsbn(this.isbn);
        product.setSku(this.sku);
        if (this.category != null) {
            product.setCategory(this.category.toEntity());
        }
        if (this.authors != null) {
            Set<Author> authorEntities = this.authors.stream()
                    .map(AuthorResponseDTO::toEntity)
                    .collect(Collectors.toSet());
            product.setAuthors(authorEntities);
        }
        product.setPriceWithPercent(this.priceWithPercent);
        return product;
    }
}
