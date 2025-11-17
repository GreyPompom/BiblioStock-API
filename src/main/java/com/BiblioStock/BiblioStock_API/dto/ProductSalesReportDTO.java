package com.BiblioStock.BiblioStock_API.dto;
import java.time.LocalDateTime;
public record ProductSalesReportDTO(
        ProductSalesSummaryDTO mostSold,
        ProductSalesSummaryDTO leastSold,
        LocalDateTime startDate,
        LocalDateTime endDate
) { }