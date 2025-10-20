
package com.BiblioStock.BiblioStock_API.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record PriceAdjustmentRequestDTO(

    @NotBlank(message = "O tipo de escopo é obrigatório (GLOBAL ou CATEGORIA).")
    String scopeType,

    @NotNull(message = "O percentual de reajuste é obrigatório.")
    @DecimalMin(value = "-1.0", inclusive = false, message = "O percentual deve ser maior que -1.0.")
    BigDecimal percent,

    Long categoryId,

    String note,

    @NotNull(message = "O identificador de quem aplicou o reajuste é obrigatório.")
    Long appliedBy

) {
}
