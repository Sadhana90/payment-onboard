package com.paymentonboard.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BadRequestException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private final String statusCode;
    private final String message;

    public BadRequestException(String statusCode, String message) {
        this.message = message;
        this.statusCode = statusCode;
    }
}
