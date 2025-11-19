package com.BiblioStock.BiblioStock_API.dto.reports;
import java.math.BigDecimal;
public record MovementHistoryItemDTO(
    Long productId,
    String productName,
    BigDecimal entries,
    BigDecimal exits,
    BigDecimal saldo

) {
    
}
