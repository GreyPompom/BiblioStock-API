package com.BiblioStock.BiblioStock_API.dto.reports;

import java.math.BigDecimal;

public record ProductPricesDTO(
    Long productId,
    String productName,
    String ISBN,
    BigDecimal priceUnit,
    BigDecimal priceWithPercent

) {
    
}
