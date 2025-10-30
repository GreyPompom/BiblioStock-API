package com.BiblioStock.BiblioStock_API.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;

public record CategoryRequestDTO(
        @NotBlank(message = "O nome da categoria é obrigatório")
        @Schema(description = "Nome da categoria", example = "Livros", required = true)
        String name,

        @NotBlank(message = "O tamanho é obrigatório - Pequeno, Médio, Grande")
        @Schema(description = "Tamanho da categoria", example = "Médio", required = true)
        String size,

        @NotBlank(message = "O tipo de embalagem é obrigatório")
        @Schema(description = "Tipo de embalagem da categoria", example = "Caixa", required = true)
        String packagingType,

        @NotNull(message = "O percentual de reajuste é obrigatório")
        @DecimalMin(value = "0.0", inclusive = false, message = "O percentual deve ser maior que zero")
        @DecimalMax(value = "100.0", message = "O percentual não pode ser maior que 100")
        @Schema(description = "Percentual de reajuste padrão da categoria", example = "10.5", required = true)
        BigDecimal defaultAdjustmentPercent
) {}
