package com.epam.esm.service.exception;

public class InvalidSortParameterException extends RuntimeException {

    public InvalidSortParameterException() {
    }

    public InvalidSortParameterException(String message) {
        super(message);
    }
}
