package com.jtech.tasklist.application.core.exceptions;

public class UnauthorizedAccessException extends RuntimeException {
    public UnauthorizedAccessException() {
        super("Acesso não autorizado a este recurso");
    }
}
