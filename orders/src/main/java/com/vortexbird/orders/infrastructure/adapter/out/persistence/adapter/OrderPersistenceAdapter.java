package com.vortexbird.orders.infrastructure.adapter.out.persistence.adapter;

import com.vortexbird.orders.application.port.out.OrderRepositoryPort;
import com.vortexbird.orders.domain.model.Order;
import com.vortexbird.orders.domain.model.OrderStatus;
import com.vortexbird.orders.infrastructure.adapter.out.persistence.entity.OrderJpaEntity;
import com.vortexbird.orders.infrastructure.adapter.out.persistence.mapper.OrderPersistenceMapper;
import com.vortexbird.orders.infrastructure.adapter.out.persistence.repository.OrderJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/*
 * Adaptador de salida que implementa OrderRepositoryPort con Spring Data JPA.
 * Para traducir entre el dominio (Order) y la entidad, y arma la consulta de filtros.
 */

@Component
@RequiredArgsConstructor
public class OrderPersistenceAdapter implements OrderRepositoryPort {

    private final OrderJpaRepository orderJpaRepository;
    private final OrderPersistenceMapper orderPersistenceMapper;

    @Override
    public Order save(Order order) {
        OrderJpaEntity entity = orderPersistenceMapper.toEntity(order);
        OrderJpaEntity savedEntity = orderJpaRepository.save(entity);
        return orderPersistenceMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Order> findById(Long id) {
        return orderJpaRepository.findById(id).map(orderPersistenceMapper::toDomain);
    }

    @Override
    public List<Order> search(OrderStatus status, String code, Boolean hasInvoice, Long createdBy) {
        String codePattern = (code == null || code.isBlank()) ? null : "%" + code.trim().toLowerCase() + "%";
        return orderJpaRepository.search(status, codePattern, hasInvoice, createdBy)
                .stream().map(orderPersistenceMapper::toDomain).toList();

    }
}
