package com.vortexbird.orders.domain.model;

import com.vortexbird.orders.domain.exception.InvalidOrderStateException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


import java.math.BigDecimal;

public class OrderTest {

    @Test
    void approvePendingOrderSucceeds() {
        Order order = Order.create("ORD-1", new BigDecimal("100"), "desc", 1L);
        order.attachInvoiceUrl("/file/test.pdf");
        order.approve(2L);
        Assertions.assertEquals(OrderStatus.APPROVED, order.getStatus());
    }

    @Test
    void approveAlreadyApprovedThrows(){
        Order order = Order.create("ORD-1", new BigDecimal("100"), "desc", 1L);
        order.attachInvoiceUrl("/file/test.pdf");
        order.approve(2L);
        Assertions.assertThrows(InvalidOrderStateException.class, () -> order.approve(3L));
    }

    @Test
    void rejectPendingOrderSucceeds(){
        Order order = Order.create("ORD-1", new BigDecimal("100"), "desc", 1L);
        order.reject(3L);
        Assertions.assertEquals(OrderStatus.REJECTED, order.getStatus());
    }

    @Test
    void approveWithoutInvoiceThrows() {
        Order order = Order.create("ORD-1", new BigDecimal("100"), "desc", 1L);
        Assertions.assertThrows(InvalidOrderStateException.class, () -> order.approve(2L));
    }
}
