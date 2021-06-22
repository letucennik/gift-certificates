package com.epam.esm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(InvalidEntityParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleInvalidEntityExceptionException(
            InvalidEntityParameterException e) {
        return buildResponse(e.getMessage(), 40001);
    }

    @ExceptionHandler(DuplicateEntityException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleDuplicateEntityException(DuplicateEntityException e) {
        return buildResponse(e.getMessage(), 40901);
    }

    @ExceptionHandler(NoSuchEntityException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse handleNoSuchEntityException(NoSuchEntityException e) {
        return buildResponse(e.getMessage(), 40401);
    }

    @ExceptionHandler(InvalidSortParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleInvalidParametersException(InvalidSortParameterException e) {
        return buildResponse(e.getMessage(), 40003);
    }

    private ExceptionResponse buildResponse(String message, int code) {
        return new ExceptionResponse(message, code);
    }
}
