package com.vortexbird.orders.infrastructure.adapter.in.rest.dto.response;

/*
 * DTO de salida con los datos (solo los que se quieren mostrar) de un usuario.
 */
public record UserResponse(String name, String email) {
}
