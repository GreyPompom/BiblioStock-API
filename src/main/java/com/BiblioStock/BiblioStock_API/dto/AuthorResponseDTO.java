package com.BiblioStock.BiblioStock_API.dto;

import com.BiblioStock.BiblioStock_API.model.Author;
import java.time.LocalDate;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "AuthorResponseDTO", description = "DTO de resposta com os dados de um autor")
public record AuthorResponseDTO(
    
        @Schema(description = "ID do autor", example = "1", required = true)
        Long id,

        @Schema(description = "Nome completo do autor", example = "J. K. Rowling", required = true)
        String fullName,

        @Schema(description = "Nacionalidade do autor", example = "Britânica", required = false)
        String nationality,

        @Schema(description = "Data de nascimento do autor", example = "1965-07-31", required = false)
        LocalDate birthDate,

        @Schema(description = "Biografia do autor", example = "Escritora britânica, famosa pela série Harry Potter", required = false)
        String biography
) {
    public static AuthorResponseDTO fromEntity(Author author) {
        return new AuthorResponseDTO(
                author.getId(),
                author.getFullName(),
                author.getNationality(),
                author.getBirthDate(),
                author.getBiography()
        );
    }
    public Author toEntity() {
        Author author = new Author();
        author.setId(this.id);
        author.setFullName(this.fullName);
        author.setNationality(this.nationality);
        author.setBirthDate(this.birthDate);
        author.setBiography(this.biography);
        return author;
    }
}
