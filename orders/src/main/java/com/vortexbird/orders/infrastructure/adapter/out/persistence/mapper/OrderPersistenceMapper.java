package com.vortexbird.orders.infrastructure.adapter.out.persistence.mapper;

import com.vortexbird.orders.domain.model.Order;
import com.vortexbird.orders.infrastructure.adapter.out.persistence.entity.OrderJpaEntity;
import org.springframework.stereotype.Component;

/*
 * Convierte entre la entidad JPA y el dominio Order (en ambos sentidos).
 * Evitando que el dominio dependa de anotaciones de persistencia.
 */
@Component
public class OrderPersistenceMapper {
    public Order toDomain(OrderJpaEntity orderJpaEntity) {
        return Order.reconstitute(orderJpaEntity.getId(),
                orderJpaEntity.getCode(),
                orderJpaEntity.getAmount(),
                orderJpaEntity.getDescription(),
                orderJpaEntity.getStatus(),
                orderJpaEntity.getInvoiceUrl(),
                orderJpaEntity.getCreatedBy(),
                orderJpaEntity.getCreatedAt(),
                orderJpaEntity.getApprovedBy(),
                orderJpaEntity.getUpdatedAt(),
                orderJpaEntity.getLastModifiedBy());
    }

    public OrderJpaEntity toEntity(Order order) {
        return new OrderJpaEntity(order.getId(),
                order.getCode(),
                order.getAmount(),
                order.getDescription(),
                order.getStatus(),
                order.getInvoiceUrl(),
                order.getCreatedBy(),
                order.getCreatedAt(),
                order.getApprovedBy(),
                order.getUpdatedAt(),
                order.getLastModifiedBy());
    }
}
