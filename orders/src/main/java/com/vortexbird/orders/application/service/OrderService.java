package com.vortexbird.orders.application.service;


import com.vortexbird.orders.application.port.in.InvoiceContent;
import com.vortexbird.orders.application.port.in.OrderUseCase;
import com.vortexbird.orders.application.port.in.OrderView;
import com.vortexbird.orders.application.port.out.OrderNotificationPort;
import com.vortexbird.orders.application.port.out.OrderRepositoryPort;
import com.vortexbird.orders.application.port.out.StoragePort;
import com.vortexbird.orders.application.port.out.UserRepositoryPort;
import com.vortexbird.orders.domain.exception.ResourceNotFoundException;
import com.vortexbird.orders.domain.model.Order;
import com.vortexbird.orders.domain.model.OrderStatus;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/*
 * Implementación de los casos de uso de órdenes.
 * Maneja el dominio (Order) y los puertos de salida (persistencia, storage, notificación, usuarios).
 * Es el corazón de la capa de aplicación, ya que aquí viven los flujos, no las reglas internas de la entidad.
 */

@Service
@RequiredArgsConstructor
public class OrderService implements OrderUseCase {

    private final OrderRepositoryPort orderRepositoryPort;
    private final StoragePort storagePort;
    private final OrderNotificationPort orderNotificationPort;
    private final UserRepositoryPort userRepositoryPort;

    @Override
    public OrderView createOrder(String code, BigDecimal amount, String description, Long createdBy) {

        Order saved = orderRepositoryPort.save(Order.create(code, amount, description, createdBy));

        return toView(saved);
    }

    @Override
    public Optional<OrderView> findById(Long id) {
        return orderRepositoryPort.findById(id).map(this::toView);
    }

    @Override
    public List<OrderView> search(OrderStatus status, String code, Boolean hasInvoice, Long createdBy) {
        return orderRepositoryPort.search(status, code, hasInvoice, createdBy).stream().map(this::toView).toList();
    }

    @Override
    public OrderView approveOrder(Long id, Long approvedBy) {
        Order order = getOrderById(id);
        order.approve(approvedBy);
        Order saved = orderRepositoryPort.save(order);
        orderNotificationPort.notifyApproved(saved);
        return toView(saved);
    }

    @Override
    public OrderView rejectOrder(Long id, Long rejectedBy) {
        Order order = getOrderById(id);
        order.reject(rejectedBy);
        Order saved = orderRepositoryPort.save(order);
        return toView(saved);
    }

    @Override
    public OrderView attachInvoice(Long orderId, byte[] content, String filename) {
        Order order = getOrderById(orderId);
        String url = storagePort.store(content, filename);
        order.attachInvoiceUrl(url);
        Order saved = orderRepositoryPort.save(order);
        return toView(saved);
    }

    private Order getOrderById(Long orderId) {
        return orderRepositoryPort.findById(orderId).orElseThrow (
                () -> new ResourceNotFoundException("Order " + orderId)
        );
    }

    private OrderView toView(Order order) {
        String createdByName = resolveName(order.getCreatedBy());
        String approvedByName = order.getApprovedBy() == null ? null : resolveName(order.getApprovedBy());
        return new OrderView(order, createdByName, approvedByName);
    }

    private String resolveName(Long userId) {
        return userRepositoryPort.findUserById(userId)
                .map(u -> u.getFirstName() + " " + u.getLastName())
                .orElse(null);
    }

    @Override
    public InvoiceContent getInvoice(Long id) {
        Order order = orderRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada"));

        String reference = order.getInvoiceUrl();
        if (reference == null || reference.isBlank()) {
            throw new ResourceNotFoundException("La orden no tiene factura");
        }

        byte[] content = storagePort.load(reference);
        String filename = reference.substring(reference.lastIndexOf('/') + 1);  // último segmento
        return new InvoiceContent(content, filename);
    }


}
