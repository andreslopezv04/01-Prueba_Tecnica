package com.vortexbird.orders.infrastructure.adapter.out.persistence.entity;

import com.vortexbird.orders.domain.model.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;


/*
 * Entidad JPA mapeada a la tabla orders.
 */
@Entity
@Table(name = "orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code;

    @Column (nullable = false)
    private BigDecimal amount;


    private String description;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    private String invoiceUrl;

    @Column(nullable = false)
    private Long createdBy;

    @Column (nullable = false)
    private LocalDateTime createdAt;


    private Long approvedBy;

    @Column (nullable = false)
    private LocalDateTime updatedAt;

    private Long lastModifiedBy;
}
