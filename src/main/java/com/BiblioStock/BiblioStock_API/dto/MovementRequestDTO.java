package com.BiblioStock.BiblioStock_API.dto;

import com.BiblioStock.BiblioStock_API.model.enums.MovementType;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "MovementRequestDTO", description = "DTO para registrar uma movimentação de estoque")
public record MovementRequestDTO(

        @NotNull(message = "O ID do produto é obrigatório")
        @Schema(description = "ID do produto", example = "1", required = true)
        Long productId,

        @NotNull(message = "O tipo de movimentação é obrigatório - Entrada ou Saída")
        @Schema(description = "Tipo de movimentação (ENTRADA ou SAÍDA)", example = "ENTRADA", required = true)
        MovementType movementType,

        @NotNull(message = "A quantidade é obrigatória")
        @DecimalMin(value = "0.01", message = "A quantidade deve ser maior que zero")
        @Schema(description = "Quantidade movimentada", example = "5.0", required = true)
        BigDecimal quantity,

        @Schema(description = "Observações ou nota sobre a movimentação", example = "Estoque inicial", required = false)
        String note,

        @Schema(description = "ID do usuário responsável pela movimentação", example = "2", required = false)
        Long userId
        ) {

}
