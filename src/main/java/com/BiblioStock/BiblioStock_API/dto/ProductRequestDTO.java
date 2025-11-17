package com.BiblioStock.BiblioStock_API.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Set;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ProductRequestDTO", description = "DTO para criação ou atualização de produto")
public record  ProductRequestDTO(

    @NotBlank(message = "O nome do produto é obrigatório.")
        @Schema(description = "Nome do produto", example = "Harry Potter e a Pedra Filosofal", required = true)
        String name,

        @NotBlank(message = "O ISBN é obrigatório.")
        @Schema(description = "Código ISBN do produto", example = "978-3-16-148410-0", required = true)
        String isbn,

        @NotBlank(message = "O SKU é obrigatório.")
        @Schema(description = "SKU do produto", example = "HPF123", required = true)
        String sku,

        @NotBlank(message = "O tipo de produto é obrigatório (Livro, Revista ou Outro)")
        @Schema(description = "Tipo de produto", example = "Livro", required = true)
        String productType,

        @NotNull(message = "O preço é obrigatório.")
        @DecimalMin(value = "0.0", inclusive = false, message = "O preço deve ser maior que zero.")
        @Schema(description = "Preço do produto", example = "49.90", required = true)
        BigDecimal price,

        @NotNull(message = "A quantidade em estoque é obrigatória.")
        @Min(value = 0, message = "A quantidade não pode ser negativa.")
        @Schema(description = "Quantidade em estoque", example = "10", required = true)
        BigDecimal stockQty,

        @NotNull(message = "A quantidade mínima é obrigatória.")
        @Min(value = 0, message = "A quantidade mínima não pode ser negativa.")
        @Schema(description = "Quantidade mínima de estoque", example = "2", required = true)
        BigDecimal minQty,

        @DecimalMin(value = "0.0", message = "A quantidade máxima não pode ser negativa")
        @Schema(description = "Quantidade máxima de estoque", example = "50", required = false)
        BigDecimal maxQty,

        @Schema(description = "Unidade de medida do produto", example = "unidade", required = false)
        String unit,

        @NotNull(message = "A categoria é obrigatória.")
        @Schema(description = "ID da categoria do produto", example = "1", required = true)
        Long categoryId,

        @NotNull(message = "O autor é obrigatório.")
        @Schema(description = "IDs dos autores do produto", example = "[1, 2]", required = true)
        Set<Long> authorIds,

        @Schema(description = "Nome da editora ou publicadora do produto", example = "Rocco", required = false)
        String publisher
) {
}
