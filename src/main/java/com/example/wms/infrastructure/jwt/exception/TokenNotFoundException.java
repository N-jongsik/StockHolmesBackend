package com.example.wms.infrastructure.jwt.exception;

public class TokenNotFoundException extends RuntimeException {
    public TokenNotFoundException(String message) {
        super(message);
    }
}
