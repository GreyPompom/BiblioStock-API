package com.BiblioStock.BiblioStock_API.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "AuthorRequestDTO", description = "DTO usado para criar ou atualizar um autor")
public record AuthorRequestDTO(

        @NotBlank(message = "O nome completo é obrigatório")
        @Schema(description = "Nome completo do autor", example = "J. K. Rowling", required = true)
        String fullName,

        @Schema(description = "Nacionalidade do autor", example = "Britânica", required = false)
        String nationality,

        @PastOrPresent(message = "A data de nascimento não pode estar no futuro")
        @Schema(description = "Data de nascimento do autor", example = "1965-07-31", required = false)
        LocalDate birthDate,

        @Schema(description = "Biografia do autor", example = "Escritora britânica, famosa pela série Harry Potter", required = false)
        String biography
) {}
