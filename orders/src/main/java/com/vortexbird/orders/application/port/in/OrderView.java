package com.vortexbird.orders.application.port.in;

import com.vortexbird.orders.domain.model.Order;

/*
 * DTO de lectura
 * una orden de dominio más los nombres ya resueltos de quién la creó y la aprobó.
 * Lo devuelve el caso de uso con el fin de que la capa de REST no tenga que resolver usuarios.
 */
public record OrderView(Order order,
                        String createdByName,
                        String approvedByName) {}