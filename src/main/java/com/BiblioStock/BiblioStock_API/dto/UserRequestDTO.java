package com.BiblioStock.BiblioStock_API.dto;

import com.BiblioStock.BiblioStock_API.model.enums.UserRole;
import jakarta.validation.constraints.*;

public record UserRequestDTO(
        @NotBlank(message = "O nome de usuário é obrigatório")
        @Size(min = 3, max = 60, message = "O nome de usuário deve ter entre 3 e 60 caracteres")
        String username,
        @NotBlank(message = "O nome completo é obrigatório")
        @Size(min = 3, max = 100, message = "O nome completo deve ter entre 3 e 100 caracteres")
        String fullName,
        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "E-mail inválido")
        String email
        ) {

}
