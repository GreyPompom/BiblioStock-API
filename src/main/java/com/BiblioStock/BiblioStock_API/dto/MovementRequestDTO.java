package com.BiblioStock.BiblioStock_API.dto;

import com.BiblioStock.BiblioStock_API.model.enums.MovementType;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record MovementRequestDTO(
        @NotNull(message = "O ID do produto é obrigatório")
        Long productId,

        @NotNull(message = "O tipo de movimentação é obrigatório")
        MovementType movementType,

        @NotNull(message = "A quantidade é obrigatória")
        @DecimalMin(value = "0.01", message = "A quantidade deve ser maior que zero")
        BigDecimal quantity,

        String note,

        Long userId
) {}
