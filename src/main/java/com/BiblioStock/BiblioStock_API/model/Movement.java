package com.BiblioStock.BiblioStock_API.model;

import com.BiblioStock.BiblioStock_API.model.enums.MovementType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "movements")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Movement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "product_name_snapshot", nullable = false)
    private String productNameSnapshot; // nome do produto no momento

    @Column(nullable = false)
    private BigDecimal quantity;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "movement_type", nullable = false)
    private MovementType movementType;

    @Column
    private String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // opcional, usuário que realizou a movimentação

    @Column(name = "movement_date")
    private LocalDateTime movementDate;
}
