package com.epam.esm.exception;

public class InvalidEntityParameterException extends RuntimeException {

    public InvalidEntityParameterException() {
    }

    public InvalidEntityParameterException(String message) {
        super(message);
    }
}
