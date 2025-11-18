package com.BiblioStock.BiblioStock_API.dto.dashboard;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "MovementSummaryDTO", description = "Resumo de movimentações de estoque")
public record MovementSummaryDTO(
        @Schema(description = "Total de movimentações registradas", example = "120")
        long totalMovements,

        @Schema(description = "Total de movimentações de entrada", example = "80")
        long totalEntradas,

        @Schema(description = "Total de movimentações de saída", example = "40")
        long totalSaidas
) {}