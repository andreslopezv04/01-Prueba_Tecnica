package com.vortexbird.orders.infrastructure.adapter.in.rest.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/*
 * DTO de entrada del login (email y password).
 */
public record LoginRequest(@NotBlank(message = "Email is required") String email,
                           @NotBlank(message = "Password is required")
                           @Size(min = 8)
                           String password) {
}
