package com.vortexbird.orders.application.port.in;

import com.vortexbird.orders.domain.model.OrderStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/*
* Puerto de entrada del modulo de órdenes
* lo implementa OrderService y lo consumen los controladores
* Esta clase es importante, ya que define el contrato de negocio sin exponer detalles
* de la infraestructura
*
*/



public interface OrderUseCase {
    OrderView createOrder(String code, BigDecimal amount, String description, Long createdBy);
    Optional<OrderView> findById (Long id);
    List<OrderView> search(OrderStatus status, String code, Boolean hasInvoice, Long createdBy);
    OrderView approveOrder(Long id, Long approvedBy);
    OrderView rejectOrder(Long id, Long rejectedBy);
    OrderView attachInvoice(Long orderId, byte[] content, String filename);
    InvoiceContent getInvoice(Long id);
}
