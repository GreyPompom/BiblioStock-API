package com.BiblioStock.BiblioStock_API.dto;

import jakarta.validation.constraints.*;

public record CategoryRequestDTO(
        @NotBlank(message = "O nome da categoria é obrigatório")
        String name,

        @NotBlank(message = "O tamanho é obrigatório")
        String size,

        @NotBlank(message = "O tipo de embalagem é obrigatório")
        String packagingType,

        @NotNull(message = "O percentual de reajuste é obrigatório")
        @DecimalMin(value = "0.0", inclusive = false, message = "O percentual deve ser maior que zero")
        @DecimalMax(value = "100.0", message = "O percentual não pode ser maior que 100")
        Double defaultAdjustmentPercent
) {}
