package com.BiblioStock.BiblioStock_API.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.BiblioStock.BiblioStock_API.model.PriceAdjustment;

@Repository
public interface PriceAdjustmentRepository extends JpaRepository<PriceAdjustment, Long> {

}
