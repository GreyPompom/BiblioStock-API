package com.BiblioStock.BiblioStock_API.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import com.BiblioStock.BiblioStock_API.model.PriceAdjustment;

@Repository
public interface PriceAdjustmentRepository extends JpaRepository<PriceAdjustment, Long> {
    Optional<PriceAdjustment> findFirstByScopeTypeOrderByAppliedAtDesc(String scopeType);

}
