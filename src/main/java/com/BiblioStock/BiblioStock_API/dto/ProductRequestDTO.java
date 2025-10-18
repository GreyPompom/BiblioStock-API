package com.BiblioStock.BiblioStock_API.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Set;

public record  ProductRequestDTO(

    @NotBlank(message = "O nome do produto é obrigatório.")
    String name,

    @NotBlank(message = "O ISBN é obrigatório.")
    String isbn,

    @NotBlank(message = "O SKU é obrigatório.")
    String sku,

    @NotBlank(message = "O tipo de produto é obrigatório (Livro, Revista ou Outro)")
    String productType,

    @NotNull(message = "O preço é obrigatório.")
    @DecimalMin(value = "0.0", inclusive = false, message = "O preço deve ser maior que zero.")
    BigDecimal price,

    @NotNull(message = "A quantidade em estoque é obrigatória.")
    @Min(value = 0, message = "A quantidade não pode ser negativa.")
    BigDecimal stockQty,

    @NotNull(message = "A quantidade mínima é obrigatória.")
    @Min(value = 0, message = "A quantidade mínima não pode ser negativa.")
    BigDecimal minQty,

    @DecimalMin(value = "0.0", message = "A quantidade máxima não pode ser negativa")
    BigDecimal maxQty,

    String unit,

    @NotNull(message = "A categoria é obrigatória.")
    Long categoryId,

    @NotNull(message = "O autor é obrigatório.")
    Set<Long> authorIds,

    String publisher
) {
}
