package com.vortexbird.orders.infrastructure.adapter.in.rest.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/*
 * DTO de entrada
 * Para crear una orden (código, monto, descripción).
 */
public record CreateOrderRequest (
        @NotBlank(message = "Code is required")
        String code,

        @NotNull @Min(value = 1)
        BigDecimal amount,

        String description
        ) {

}
