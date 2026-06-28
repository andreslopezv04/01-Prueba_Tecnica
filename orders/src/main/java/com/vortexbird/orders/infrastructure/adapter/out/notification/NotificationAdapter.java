package com.vortexbird.orders.infrastructure.adapter.out.notification;

import com.vortexbird.orders.application.port.out.OrderNotificationPort;
import com.vortexbird.orders.domain.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

/*
 * Adaptador de salida que implementa OrderNotificationPort.
 * Hace el POST al sistema externo cuando una orden se aprueba,
 * sin frenar el flujo principal
 */
@Component
@Slf4j
public class NotificationAdapter implements OrderNotificationPort {

    @Value("${notification.url}")
    private String notificationUrl;
    private final RestClient restClient = createRestClient();

    private static RestClient createRestClient() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(2000);
        factory.setReadTimeout(3000);
        return RestClient.builder().requestFactory(factory).build();
    }

    @Override
    public void notifyApproved(Order order) {
        try {
            Map<String, Object> body = Map.of(
                    "orderId", order.getId(),
                    "code", order.getCode(),
                    "amount", order.getAmount(),
                    "status", order.getStatus().name()
            );

            restClient.post()
                    .uri(notificationUrl)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .toBodilessEntity();

            log.info("Notificación enviada para la orden {}", order.getId());
        } catch (Exception e) {
            log.error("Falló la notificación de la orden {}: {}", order.getId(), e.getMessage());
        }
    }
}
