package com.BiblioStock.BiblioStock_API.repository;

import com.BiblioStock.BiblioStock_API.model.Movement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovementRepository extends JpaRepository<Movement, Long> {
}
