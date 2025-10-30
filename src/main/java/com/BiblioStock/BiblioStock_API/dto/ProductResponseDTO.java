package com.BiblioStock.BiblioStock_API.dto;

import com.BiblioStock.BiblioStock_API.model.Product;
import java.util.Set;
import java.math.BigDecimal;
import com.BiblioStock.BiblioStock_API.model.Author;
import java.util.stream.Collectors;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ProductResponseDTO", description = "DTO de resposta com os dados de um produto")
public record ProductResponseDTO(
         @Schema(description = "ID do produto", example = "1", required = true)
        Long id,

        @Schema(description = "Nome do produto", example = "Harry Potter e a Pedra Filosofal", required = true)
        String name,

        @Schema(description = "Tipo de produto", example = "Livro", required = true)
        String productType,

        @Schema(description = "Preço do produto", example = "49.90", required = true)
        BigDecimal price,

        @Schema(description = "Unidade de medida do produto", example = "unidade", required = false)
        String unit,

        @Schema(description = "Quantidade em estoque", example = "10", required = true)
        BigDecimal stockQty,

        @Schema(description = "Quantidade mínima de estoque", example = "2", required = true)
        BigDecimal minQty,

        @Schema(description = "Quantidade máxima de estoque", example = "50", required = false)
        BigDecimal maxQty,

        @Schema(description = "Editora do produto", example = "Rocco", required = false)
        String publisher,

        @Schema(description = "Código ISBN do produto", example = "978-3-16-148410-0", required = true)
        String isbn,

        @Schema(description = "Categoria do produto", required = true)
        CategoryResponseDTO category,

        @Schema(description = "Autores do produto", required = true)
        Set<AuthorResponseDTO> authors,

        @Schema(description = "Preço calculado considerando percentuais de reajuste", example = "54.90", required = false)
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
                p.getCategory() != null ? CategoryResponseDTO.fromEntity(p.getCategory()) : null,
                p.getAuthors() != null
                        ? p.getAuthors().stream()
                            .map(AuthorResponseDTO::fromEntity)
                            .collect(Collectors.toSet())
                        : Set.of(),
                p.getPriceWithPercent()
        );
    }
}
