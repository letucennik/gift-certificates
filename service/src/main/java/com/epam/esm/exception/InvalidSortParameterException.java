package com.epam.esm.exception;

public class InvalidSortParameterException extends RuntimeException {

    public InvalidSortParameterException() {
    }

    public InvalidSortParameterException(String message) {
        super(message);
    }
}
