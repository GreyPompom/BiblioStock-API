package com.BiblioStock.BiblioStock_API.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.BiblioStock.BiblioStock_API.model.Category;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
      Optional<Category> findByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCase(String name);

}
