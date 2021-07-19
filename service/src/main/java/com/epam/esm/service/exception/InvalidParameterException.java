package com.epam.esm.service.exception;

public class InvalidParameterException extends RuntimeException {

    public InvalidParameterException() {
    }

    public InvalidParameterException(String message) {
        super(message);
    }
}
