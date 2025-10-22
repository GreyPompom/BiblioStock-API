package com.BiblioStock.BiblioStock_API.exception;

// lança 404 quando um recurso não é encontrado
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
