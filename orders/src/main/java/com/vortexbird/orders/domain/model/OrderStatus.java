package com.vortexbird.orders.domain.model;

/*
 * Estados posibles de una Orden de Pago.
 * Lo usa el dominio Order para controlar qué transiciones son válidas.
 */
public enum OrderStatus {
    PENDING,
    ARCHIVED,
    APPROVED,
    REJECTED
}
