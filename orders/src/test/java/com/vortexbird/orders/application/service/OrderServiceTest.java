package com.vortexbird.orders.application.service;

import com.vortexbird.orders.application.port.in.OrderView;
import com.vortexbird.orders.application.port.out.OrderNotificationPort;
import com.vortexbird.orders.application.port.out.OrderRepositoryPort;
import com.vortexbird.orders.application.port.out.UserRepositoryPort;
import com.vortexbird.orders.domain.exception.InvalidOrderStateException;
import com.vortexbird.orders.domain.exception.ResourceNotFoundException;
import com.vortexbird.orders.domain.model.Order;
import com.vortexbird.orders.domain.model.OrderStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepositoryPort orderRepositoryPort;
    @Mock
    private OrderNotificationPort orderNotificationPort;
    @Mock
    private UserRepositoryPort userRepositoryPort;

    @InjectMocks
    private OrderService orderService;



    @Test
    void createOrderReturnsSavedOrder() {

        Order ordenSimulada = Order.reconstitute(1L,"ORD-1", new BigDecimal("100"), "desc", OrderStatus.PENDING, null,1L, LocalDateTime.now(),2L,LocalDateTime.now(), 1L );
        when(orderRepositoryPort.save(any())).thenReturn(ordenSimulada);

        OrderView resultado = orderService.createOrder("ORD-1", new BigDecimal("100"), "desc", 1L);

        assertEquals(ordenSimulada, resultado.order());

        verify(orderRepositoryPort).save(any());
    }

    @Test
    void approveOrderWithoutInvoiceSucceeds(){
        Order ordenSimulada = Order.create("ORD-1", new BigDecimal("100"), "desc", 1L);
        when(orderRepositoryPort.findById(1L)).thenReturn(Optional.of(ordenSimulada));
        Assertions.assertThrows(InvalidOrderStateException.class, () -> orderService.approveOrder(1L, 2L));

    }

    @Test
    void approveOrderWithInvoiceSucceeds(){
        Order orderSimulada = Order.create("ORD-1", new BigDecimal("100"), "desc", 1L);
        orderSimulada.attachInvoiceUrl("/files/test.pdf");
        when(orderRepositoryPort.findById(1L)).thenReturn(Optional.of(orderSimulada));
        when(orderRepositoryPort.save(any())).thenReturn(orderSimulada);

        OrderView resultado = orderService.approveOrder(1L, 2L);
        assertEquals(OrderStatus.APPROVED, resultado.order().getStatus());
        verify(orderRepositoryPort).save(any());
        verify(orderNotificationPort).notifyApproved(any());
    }

    @Test
    void approveNonexistentOrderThrows (){
        when(orderRepositoryPort.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> orderService.approveOrder(1L, 2L));
    }
}
