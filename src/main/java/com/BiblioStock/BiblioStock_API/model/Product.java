package com.BiblioStock.BiblioStock_API.model;

import lombok.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String sku;

    @NotBlank(message = "O nome do produto é obrigatório.")
    @Column(columnDefinition = "TEXT")
    private String name;

    @NotBlank(message = "O ISBN é obrigatório.")
    @Column(columnDefinition = "TEXT")
    private String isbn;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String productType; // Livro, Revista ou Outro

    @NotNull(message = "O preço é obrigatório.")
    @DecimalMin(value = "0.0", inclusive = false, message = "O preço deve ser maior que zero.")
    private BigDecimal price;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String unit = "unidade";

    private String publisher;

    @NotNull(message = "A quantidade em estoque é obrigatória.")
    @Min(value = 0, message = "A quantidade não pode ser negativa.")
    private BigDecimal stockQty;

    @NotNull(message = "A quantidade mínima é obrigatória.")
    @Min(value = 0, message = "A quantidade mínima não pode ser negativa.")
    private BigDecimal minQty;

    @NotNull(message = "A quantidade maxima é obrigatória.")
    @Min(value = 0, message = "A quantidade máxima não pode ser negativa.")
    private BigDecimal maxQty;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "product_authors",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private Set<Author> authors = new HashSet<>();

    @Column(name = "created_at")
    private OffsetDateTime createdAt = OffsetDateTime.now();
}
