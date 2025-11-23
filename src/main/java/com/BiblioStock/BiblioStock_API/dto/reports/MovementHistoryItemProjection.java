package com.BiblioStock.BiblioStock_API.dto.reports;

import java.math.BigDecimal;

public interface MovementHistoryItemProjection {
    Long getProductId();
    String getProductNameSnapshot();
    BigDecimal getTotalEntrada();
    BigDecimal getTotalSaida();
    BigDecimal getSaldo();
}