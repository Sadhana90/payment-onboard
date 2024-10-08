package com.paymentonboard.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    
    private final String statusCode;
    private final String message;

    public BusinessException(String statusCode, String message) {
        this.message = message;
        this.statusCode = statusCode;
    }

}
