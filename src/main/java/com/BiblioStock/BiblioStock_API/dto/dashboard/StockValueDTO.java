package com.BiblioStock.BiblioStock_API.dto.dashboard;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(name = "StockValueDTO", description = "Valor total do estoque")
public record StockValueDTO(
        @Schema(description = "Valor total do estoque em moeda", example = "1520.75")
        BigDecimal totalStockValue,
        @Schema(description = "Porcentagem do valor total do estoque", example = "75.5")
        BigDecimal totalStockValuePercentage,
        @Schema(description = "Total de produtos no estoque", example = "150")
        Long totalProducts 
) {}