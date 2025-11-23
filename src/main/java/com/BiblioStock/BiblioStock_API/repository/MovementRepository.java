package com.BiblioStock.BiblioStock_API.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.BiblioStock.BiblioStock_API.dto.reports.MovementHistoryItemDTO;
import com.BiblioStock.BiblioStock_API.dto.reports.MovementHistoryItemProjection;
import com.BiblioStock.BiblioStock_API.dto.reports.ProductSalesSummaryDTO;
import com.BiblioStock.BiblioStock_API.dto.reports.ProductSalesSummaryProjection;
import com.BiblioStock.BiblioStock_API.model.Movement;

@Repository
public interface MovementRepository extends JpaRepository<Movement, Long> {

    @Query(value = """
        select 
            m.product_id as productId,
            p.name as productName,
            sum(m.quantity) as totalQuantity
        from movements m
        join products p on p.id = m.product_id
        where m.movement_type = 'SAIDA'
        group by m.product_id, p.name
        order by sum(m.quantity) desc
        """,
            nativeQuery = true)
    List<ProductSalesSummaryProjection> findProductSalesSummaryNative();

// produtos mais vendidos entre datas
    @Query(value = """
        select 
            m.product_id as productId,
            p.name as productName,
            sum(m.quantity) as totalQuantity
        from movements m
        join products p on p.id = m.product_id
        where m.movement_type = 'SAIDA'
          and m.movement_date >= :startDate
          and m.movement_date <= :endDate
        group by m.product_id, p.name
        order by sum(m.quantity) desc
        """,
            nativeQuery = true)
    List<ProductSalesSummaryProjection> findProductSalesSummaryBetweenNative(
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

    @Query(value = """
        select 
            m.product_id as productId,
            m.product_name_snapshot as productNameSnapshot,
            sum(case when m.movement_type = 'ENTRADA' then m.quantity else 0 end) as totalEntrada,
            sum(case when m.movement_type = 'SAIDA' then m.quantity else 0 end) as totalSaida,
            (
              sum(case when m.movement_type = 'ENTRADA' then m.quantity else 0 end)
              - sum(case when m.movement_type = 'SAIDA' then m.quantity else 0 end)
            ) as saldo
        from movements m
        group by m.product_id, m.product_name_snapshot
        order by m.product_name_snapshot asc
        """,
            nativeQuery = true)
    List<MovementHistoryItemProjection> findMovementHistoryNative();
}
