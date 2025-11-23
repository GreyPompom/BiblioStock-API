package com.BiblioStock.BiblioStock_API.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.BiblioStock.BiblioStock_API.model.Author;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    boolean existsByFullNameIgnoreCase(String fullName);
    Optional<Author> findByFullNameIgnoreCase(String fullName);
    List<Author> findAllById(Iterable<Long> ids);

    @Query("SELECT COUNT(p) FROM Product p JOIN p.authors a WHERE a.id = :authorId")
    Integer countProductsByAuthor(Long authorId);
}
