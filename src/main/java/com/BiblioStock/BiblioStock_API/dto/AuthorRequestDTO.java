package com.BiblioStock.BiblioStock_API.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record AuthorRequestDTO(

        @NotBlank(message = "O nome completo é obrigatório")
        String fullName,

        String nationality,

        @PastOrPresent(message = "A data de nascimento não pode estar no futuro")
        LocalDate birthDate,

        String biography
) {}
