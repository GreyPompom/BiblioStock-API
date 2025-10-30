package com.BiblioStock.BiblioStock_API.dto;

import com.BiblioStock.BiblioStock_API.model.enums.UserRole;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "UserRequestDTO", description = "DTO para criação ou atualização de usuário")
public record UserRequestDTO(
        
        @NotBlank(message = "O nome de usuário é obrigatório")
        @Size(min = 3, max = 60, message = "O nome de usuário deve ter entre 3 e 60 caracteres")
        @Schema(description = "Nome de usuário", example = "jsilva", required = true)
        String username,

        @NotBlank(message = "O nome completo é obrigatório")
        @Size(min = 3, max = 100, message = "O nome completo deve ter entre 3 e 100 caracteres")
        @Schema(description = "Nome completo do usuário", example = "João da Silva", required = true)
        String fullName,

        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "E-mail inválido")
        @Schema(description = "E-mail do usuário", example = "joao.silva@email.com", required = true)
        String email
        ) {
}
