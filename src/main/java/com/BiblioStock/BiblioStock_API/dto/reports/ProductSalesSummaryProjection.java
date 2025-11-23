package com.BiblioStock.BiblioStock_API.dto.reports;
import java.math.BigDecimal;
public interface ProductSalesSummaryProjection {
    Long getProductId();
    String getProductName();
    BigDecimal getTotalQuantity();
}