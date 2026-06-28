package com.vortexbird.orders.infrastructure.adapter.in.rest.dto.response;

import com.vortexbird.orders.domain.model.OrderStatus;

import java.math.BigDecimal;

/*
 * DTO de salida de una orden.
 */
public record OrderResponse(
        Long id,
        String code,
        BigDecimal amount,
        String description,
        OrderStatus status,
        String createdByName,
        String approvedByName,
        boolean hasInvoice
) {

}
