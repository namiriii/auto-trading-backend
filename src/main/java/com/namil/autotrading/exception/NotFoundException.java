package com.namil.autotrading.exception;

import org.springframework.web.bind.annotation.RestControllerAdvice;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
