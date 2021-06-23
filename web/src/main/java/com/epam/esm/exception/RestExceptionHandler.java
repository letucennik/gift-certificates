package com.epam.esm.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@RestControllerAdvice
public class RestExceptionHandler {

    private static final List<String> AVAILABLE_LOCALES = Arrays.asList("en", "ru");
    private static final Locale DEFAULT_LOCALE = new Locale("ru");

    private final ResourceBundleMessageSource bundleMessageSource;

    @Autowired
    public RestExceptionHandler(ResourceBundleMessageSource bundleMessageSource) {
        this.bundleMessageSource = bundleMessageSource;
    }

    @ExceptionHandler(InvalidEntityParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleInvalidEntityExceptionException(
            InvalidEntityParameterException e, Locale locale) {
        return buildResponse(resolveResourceBundleMessage(e.getMessage(), locale), 40001);
    }

    @ExceptionHandler(DuplicateEntityException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleDuplicateEntityException(DuplicateEntityException e, Locale locale) {
        return buildResponse(resolveResourceBundleMessage(e.getMessage(), locale), 40901);
    }

    @ExceptionHandler(NoSuchEntityException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse handleNoSuchEntityException(NoSuchEntityException e, Locale locale) {
        return buildResponse(resolveResourceBundleMessage(e.getMessage(), locale), 40401);
    }

    @ExceptionHandler(InvalidSortParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleInvalidParametersException(InvalidSortParameterException e, Locale locale) {
        return buildResponse(resolveResourceBundleMessage(e.getMessage(), locale), 40003);
    }

    private ExceptionResponse buildResponse(String message, int code) {
        return new ExceptionResponse(message, code);
    }

    private String resolveResourceBundleMessage(String key, Locale locale) {
        if (!AVAILABLE_LOCALES.contains(locale.toString())) {
            locale = DEFAULT_LOCALE;
        }
        return bundleMessageSource.getMessage(key, null, locale);
    }
}
