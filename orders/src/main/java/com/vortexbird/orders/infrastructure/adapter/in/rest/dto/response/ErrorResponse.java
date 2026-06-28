package com.vortexbird.orders.infrastructure.adapter.in.rest.dto.response;

import java.time.LocalDateTime;

/*
 * Cuerpo estándar de error (timestamp, código, mensaje)
 * que devuelve el manejo centralizado al frontend.
 */

public record ErrorResponse(LocalDateTime timestamp,
                            int status,
                            String message)
{}