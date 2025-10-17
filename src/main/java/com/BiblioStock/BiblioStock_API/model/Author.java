package com.BiblioStock.BiblioStock_API.model;

import java.time.OffsetDateTime;

import jakarta.persistence.*;
import lombok.*;


@Table(name = "authors")
@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Author {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="full_name" nullable = false)
    private String full_name;

    private String nationality;

    private LocalDate birthDate;

    @Column(columnDefinition = "TEXT")
    private String biography;

    @Column(name = "created_at")
    private OffsetDateTime createdAt = OffsetDateTime.now();

}