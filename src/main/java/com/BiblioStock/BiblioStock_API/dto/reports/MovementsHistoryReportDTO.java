package com.BiblioStock.BiblioStock_API.dto.reports;
import java.math.BigDecimal;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;


@Schema(name= "MovementsHistoryReportDTO",description = "Resposta do relatório de movimentacao de estoque")
public record MovementsHistoryReportDTO(

        List<MovementHistoryItemDTO> movements,

        ProductSalesSummaryDTO mostSold,
        ProductSalesSummaryDTO leastSold

) {}
