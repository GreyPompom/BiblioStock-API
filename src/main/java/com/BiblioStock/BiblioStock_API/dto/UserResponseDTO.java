package com.BiblioStock.BiblioStock_API.dto;

import com.BiblioStock.BiblioStock_API.model.User;
import com.BiblioStock.BiblioStock_API.model.enums.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(name = "UserResponseDTO", description = "DTO de resposta com os dados de um usuário")
public record UserResponseDTO(
        
        @Schema(description = "ID do usuário", example = "1", required = true)
        Long id,

        @Schema(description = "Nome de usuário", example = "jsilva", required = true)
        String username,

        @Schema(description = "Nome completo do usuário", example = "João da Silva", required = true)
        String fullName,

        @Schema(description = "E-mail do usuário", example = "joao.silva@email.com", required = true)
        String email, 

        @Schema(description = "Data de criação do usuário", example = "2025-10-28T15:30:00", required = true)
        LocalDateTime createdAt
        ) {

    public static UserResponseDTO fromEntity(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getFullName(),
                user.getEmail(),
                user.getCreatedAt()
        );
    }
}
