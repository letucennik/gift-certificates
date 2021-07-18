package com.epam.esm.controller.exception;

import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class AcceptLocaleResolver extends AcceptHeaderLocaleResolver {

    private static final List<Locale> AVAILABLE_LOCALES = Arrays.asList(new Locale("en"), new Locale("en"));

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        String headerLang = request.getHeader("Accept-Language");
        return headerLang == null || headerLang.isEmpty()
                ? Locale.getDefault()
                : Locale.lookup(Locale.LanguageRange.parse(headerLang), AVAILABLE_LOCALES);
    }
}
