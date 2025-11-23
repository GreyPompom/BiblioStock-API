package com.BiblioStock.BiblioStock_API.dto.reports;
import java.math.BigDecimal;

public record ProductSalesSummaryDTO(
        Long productId,
        String productName,
        BigDecimal totalSold
) { }