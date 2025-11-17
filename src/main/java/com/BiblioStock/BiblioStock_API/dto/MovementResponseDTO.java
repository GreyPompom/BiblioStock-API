package com.BiblioStock.BiblioStock_API.dto;

import com.BiblioStock.BiblioStock_API.model.Movement;
import com.BiblioStock.BiblioStock_API.model.enums.MovementType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "MovementResponseDTO", description = "DTO de resposta com os dados de uma movimentação de estoque")
public record MovementResponseDTO(
    
        @Schema(description = "ID da movimentação", example = "1", required = true)
        Long id,

        @Schema(description = "ID do produto", example = "1", required = true)
        Long productId,

        @Schema(description = "Nome do produto", example = "Livro Harry Potter", required = true)
        String productName,

        @Schema(description = "Quantidade movimentada", example = "5.0", required = true)
        BigDecimal quantity,

        @Schema(description = "Tipo de movimentação (ENTRADA ou SAÍDA)", example = "ENTRADA", required = true)
        MovementType movementType,

        @Schema(description = "Observações ou nota sobre a movimentação", example = "Estoque inicial", required = false)
        String note,

        @Schema(description = "ID do usuário responsável pela movimentação", example = "2", required = false)
        Long userId,

        @Schema(description = "Nome do usuário responsável pela movimentação", example = "João Silva", required = false)
        String userName,

        @Schema(description = "Data e hora da movimentação", example = "2025-10-28T15:30:00", required = true)
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
