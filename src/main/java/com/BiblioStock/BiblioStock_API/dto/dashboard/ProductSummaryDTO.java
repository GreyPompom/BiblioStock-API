package com.BiblioStock.BiblioStock_API.dto.dashboard;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(name = "ProductSummaryDTO", description = "Resumo de produto para o dashboard")
public record ProductSummaryDTO(
        Long id,
        String name,
        BigDecimal price,
        BigDecimal stockQty
) {}