package com.vortexbird.orders.application.port.out;

import com.vortexbird.orders.domain.model.Order;
import com.vortexbird.orders.domain.model.OrderStatus;

import java.util.List;
import java.util.Optional;

/*
 * Puerto de salida hacia la persistencia de órdenes.
 * Lo implementa el adaptador JPA.
 * Existe con el fin de aíslar el dominio de la base de datos.
 */
public interface OrderRepositoryPort {
    Order save (Order order);
    Optional<Order> findById(Long id);
    List<Order> search(OrderStatus status, String code, Boolean hasInvoice, Long createdBy);
}
