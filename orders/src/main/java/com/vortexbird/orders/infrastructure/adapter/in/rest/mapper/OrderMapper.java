package com.vortexbird.orders.infrastructure.adapter.in.rest.mapper;

import com.vortexbird.orders.application.port.in.OrderView;
import com.vortexbird.orders.domain.model.Order;
import com.vortexbird.orders.infrastructure.adapter.in.rest.dto.response.OrderResponse;
import org.springframework.stereotype.Component;

/*
 * Mapeo entre la vista de dominio (OrderView) y los DTOs REST.
 * Con el fin de mantener la capa web separada del modelo de la aplicación.
 */
@Component
public class OrderMapper {

    public OrderResponse toResponse(OrderView view) {
        Order order = view.order();
        boolean hasInvoice = order.getInvoiceUrl() != null && !order.getInvoiceUrl().isBlank();

        return new OrderResponse(order.getId(), order.getCode(), order.getAmount(),
                order.getDescription(), order.getStatus(),
                view.createdByName(), view.approvedByName(), hasInvoice);
    }
}
