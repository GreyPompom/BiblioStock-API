package com.BiblioStock.BiblioStock_API.dto.dashboard;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(name = "DashboardOverviewDTO", description = "Visão geral do dashboard")
public record DashboardOverviewDTO(

        @Schema(description = "Total de produtos cadastrados")
        ProductCountDTO productCount,

        @Schema(description = "Últimos produtos cadastrados")
        List<ProductSummaryDTO> lastProducts,

        @Schema(description = "Resumo das movimentações de estoque")
        MovementSummaryDTO movementSummary,

        @Schema(description = "Valor total do estoque")
        StockValueDTO stockValue
) {}
