package com.vortexbird.orders.domain.exception;

// Excepción de dominio y se lanza cuando se intenta una transición ilegal.
public class InvalidOrderStateException extends RuntimeException {
    public InvalidOrderStateException(String message) {
        super(message);
    }
}