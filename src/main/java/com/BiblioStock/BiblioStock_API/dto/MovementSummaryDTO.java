package com.BiblioStock.BiblioStock_API.dto;

import java.math.BigDecimal;
import java.util.List;

public record MovementSummaryDTO(
        Long productId,
        String productName,
        BigDecimal totalQuantity,
        String movementType,
        List<MovementResponseDTO> movementHistory
) {}
