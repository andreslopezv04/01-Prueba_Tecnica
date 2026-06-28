package com.vortexbird.orders.domain.exception;

/*
 * Excepción de dominio para recursos inexistentes.
 * El GlobalExceptionHandler la traduce a un HTTP 404.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String entity, Long id) {
        super(entity + " with id " + id + " not found");
    }
}
