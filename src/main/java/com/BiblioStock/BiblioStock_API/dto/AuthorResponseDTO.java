package com.BiblioStock.BiblioStock_API.dto;

import com.BiblioStock.BiblioStock_API.model.Author;
import java.time.LocalDate;

public record AuthorResponseDTO(
        Long id,
        String fullName,
        String nationality,
        LocalDate birthDate,
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
