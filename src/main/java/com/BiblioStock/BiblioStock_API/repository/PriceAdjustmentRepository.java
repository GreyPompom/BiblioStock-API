package com.BiblioStock.BiblioStock_API.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import com.BiblioStock.BiblioStock_API.model.PriceAdjustment;
import java.util.List;

@Repository
public interface PriceAdjustmentRepository extends JpaRepository<PriceAdjustment, Long> {

    // Retorna o ajuste mais recente por escopo (ex: último global aplicado)
    Optional<PriceAdjustment> findFirstByScopeTypeOrderByAppliedAtDesc(String scopeType);

    // Retorna todos os ajustes filtrando por escopo (ex: "CATEGORY")
    List<PriceAdjustment> findByScopeType(String scopeType);

    // Retorna um ajuste específico por escopo e categoria
    Optional<PriceAdjustment> findByScopeTypeAndCategoryId(String scopeType, Long categoryId);

}
