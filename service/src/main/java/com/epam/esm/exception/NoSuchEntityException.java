package com.epam.esm.exception;

public class NoSuchEntityException extends RuntimeException {

    public NoSuchEntityException() {
    }

    public NoSuchEntityException(String message) {
        super(message);
    }
}
