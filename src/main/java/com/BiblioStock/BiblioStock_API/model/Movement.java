package com.BiblioStock.BiblioStock_API.model;

import com.BiblioStock.BiblioStock_API.model.enums.MovementType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "movements")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Movement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private String productNameSnapshot; // nome do produto no momento

    @Column(nullable = false)
    private BigDecimal quantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MovementType movementType; // ENTRADA ou SAIDA

    @Column
    private String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // opcional, usuário que realizou a movimentação

    @Column(nullable = false)
    private LocalDateTime movementDate;

    @PrePersist
    public void prePersist() {
        movementDate = LocalDateTime.now();
        productNameSnapshot = product.getName();
    }
}
