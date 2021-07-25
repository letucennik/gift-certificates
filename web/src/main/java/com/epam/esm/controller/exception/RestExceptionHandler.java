package com.epam.esm.controller.exception;

import com.epam.esm.service.exception.DuplicateEntityException;
import com.epam.esm.service.exception.InvalidParameterException;
import com.epam.esm.service.exception.InvalidSortParameterException;
import com.epam.esm.service.exception.NoSuchEntityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

@RestControllerAdvice
public class RestExceptionHandler {

    public static final int INVALID_ENTITY_PARAMETER_CODE = 40001;
    public static final int ENTITY_ALREADY_EXISTS_CODE = 40901;
    public static final int ENTITY_NOT_FOUND_CODE = 40401;
    public static final int INVALID_REQUEST_PARAMETER_CODE = 40003;
    public static final int FORBIDDEN_REQUEST_CODE=50106;

    private final ResourceBundleMessageSource bundleMessageSource;

    @Autowired
    public RestExceptionHandler(ResourceBundleMessageSource bundleMessageSource) {
        this.bundleMessageSource = bundleMessageSource;
    }

    @ExceptionHandler(InvalidParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleInvalidEntityExceptionException(
            InvalidParameterException e, Locale locale) {
        return buildResponse(resolveResourceBundleMessage(e.getMessage(), locale), INVALID_ENTITY_PARAMETER_CODE);
    }

    @ExceptionHandler(DuplicateEntityException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleDuplicateEntityException(DuplicateEntityException e, Locale locale) {
        return buildResponse(resolveResourceBundleMessage(e.getMessage(), locale), ENTITY_ALREADY_EXISTS_CODE);
    }

    @ExceptionHandler(NoSuchEntityException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse handleNoSuchEntityException(NoSuchEntityException e, Locale locale) {
        return buildResponse(resolveResourceBundleMessage(e.getMessage(), locale), ENTITY_NOT_FOUND_CODE);
    }

    @ExceptionHandler(InvalidSortParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleInvalidParametersException(InvalidSortParameterException e, Locale locale) {
        return buildResponse(resolveResourceBundleMessage(e.getMessage(), locale), INVALID_REQUEST_PARAMETER_CODE);
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionResponse handleAuthenticationException(AuthenticationException e, Locale locale) {
        return buildResponse(e.getMessage(), FORBIDDEN_REQUEST_CODE);
    }

    private ExceptionResponse buildResponse(String message, int code) {
        return new ExceptionResponse(message, code);
    }

    private String resolveResourceBundleMessage(String key, Locale locale) {
        return bundleMessageSource.getMessage(key, null, locale);
    }
}
