package com.BiblioStock.BiblioStock_API.dto.reports;

import java.math.BigDecimal;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name= "BalanceResponseDTO",description = "Resposta do relatório de balanço (RF025)")
public record BalanceResponseDTO(

        @Schema(description = "Lista de itens do balanço")
        List<BalanceRequestDTO> items,

        @Schema(description = "Valor total do estoque", example = "15890.45")
        BigDecimal totalValue,
        @Schema(description = "Porcentagem do valor total do estoque", example = "80.5")
        BigDecimal totalValuePercentage

) {}
