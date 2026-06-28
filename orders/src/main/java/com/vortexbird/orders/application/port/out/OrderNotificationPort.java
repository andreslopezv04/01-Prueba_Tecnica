package com.vortexbird.orders.application.port.out;

import com.vortexbird.orders.domain.model.Order;

/*
 * Puerto de salida para notificar a un sistema externo cuando una orden se aprueba.
 * Lo implementa el adaptador HTTP.
 * Permite desacoplar la integración externa del caso de uso.
 */
public interface OrderNotificationPort {
    void notifyApproved(Order order);
}
