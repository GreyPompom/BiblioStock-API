package com.BiblioStock.BiblioStock_API.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.BiblioStock.BiblioStock_API.dto.ProductSalesSummaryDTO;
import com.BiblioStock.BiblioStock_API.model.Movement;
import com.BiblioStock.BiblioStock_API.model.enums.MovementType;

@Repository
public interface MovementRepository extends JpaRepository<Movement, Long> {

   @Query("""
        select new com.BiblioStock.BiblioStock_API.dto.ProductSalesSummaryDTO(
            m.product.id,
            m.product.name,
            sum(m.quantity)
        )
        from Movement m
        where m.movementType = com.BiblioStock.BiblioStock_API.model.enums.MovementType.SAIDA
        group by m.product.id, m.product.name
        order by sum(m.quantity) desc
    """)
    List<ProductSalesSummaryDTO> findProductSalesSummary();

    @Query("""
        select new com.BiblioStock.BiblioStock_API.dto.ProductSalesSummaryDTO(
            m.product.id,
            m.product.name,
            sum(m.quantity)
        )
        from Movement m
        where m.movementType = com.BiblioStock.BiblioStock_API.model.enums.MovementType.SAIDA
          and m.movementDate >= :startDate
          and m.movementDate <= :endDate
        group by m.product.id, m.product.name
        order by sum(m.quantity) desc
    """)
    List<ProductSalesSummaryDTO> findProductSalesSummaryBetween(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    long count();

    Page<Movement> findByMovementDateBetween(
            LocalDateTime startDate,
            LocalDateTime endDate,
            Pageable pageable
    );

     @Query(
        value = """
                select count(m.id)
                from movements m
                where m.movement_type = cast(:movementType as movement_type)
                """,
        nativeQuery = true
    )
    long countByMovementType(@Param("movementType") String movementType);
}
