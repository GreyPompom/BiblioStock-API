package com.BiblioStock.BiblioStock_API.repository;

import com.BiblioStock.BiblioStock_API.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    boolean existsByFullNameIgnoreCase(String fullName);
    Optional<Author> findByFullNameIgnoreCase(String fullName);
    List<Author> findAllById(Iterable<Long> ids);
}
