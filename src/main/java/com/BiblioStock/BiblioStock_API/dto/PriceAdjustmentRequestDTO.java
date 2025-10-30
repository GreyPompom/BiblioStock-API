package com.BiblioStock.BiblioStock_API.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "PriceAdjustmentRequestDTO", description = "DTO para aplicar reajustes de preço")
public record PriceAdjustmentRequestDTO(
        
        @NotBlank(message = "O tipo de escopo é obrigatório (GLOBAL ou CATEGORIA).")
        @Schema(description = "Escopo do reajuste (GLOBAL ou CATEGORIA)", example = "GLOBAL", required = true)
        String scopeType,

        @NotNull(message = "O percentual de reajuste é obrigatório.")
        @DecimalMin(value = "-1.0", inclusive = false, message = "O percentual deve ser maior que -1.0.")
        @Schema(description = "Percentual de reajuste aplicado", example = "10.0", required = true)
        BigDecimal percent,

        @Schema(description = "ID da categoria, obrigatório se scopeType = CATEGORIA", example = "1", required = false)
        Long categoryId,

        @Schema(description = "Observações ou nota sobre o reajuste", example = "Promoção de verão", required = false)
        String note
        ) {

}
