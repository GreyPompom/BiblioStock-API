package com.BiblioStock.BiblioStock_API.dto;

import com.BiblioStock.BiblioStock_API.model.Movement;
import com.BiblioStock.BiblioStock_API.model.enums.MovementType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MovementResponseDTO(
        Long id,
        Long productId,
        String productName,
        BigDecimal quantity,
        MovementType movementType,
        String note,
        Long userId,
        String userName,
        LocalDateTime movementDate
) {
    public static MovementResponseDTO fromEntity(Movement m) {
        return new MovementResponseDTO(
                m.getId(),
                m.getProduct().getId(),
                m.getProductNameSnapshot(),
                m.getQuantity(),
                m.getMovementType(),
                m.getNote(),
                m.getUser() != null ? m.getUser().getId() : null,
                m.getUser() != null ? m.getUser().getUsername() : null,
                m.getMovementDate()
        );
    }
}
