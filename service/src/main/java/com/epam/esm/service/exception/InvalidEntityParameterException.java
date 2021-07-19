package com.epam.esm.service.exception;

public class InvalidEntityParameterException extends RuntimeException {

    public InvalidEntityParameterException() {
    }

    public InvalidEntityParameterException(String message) {
        super(message);
    }
}
