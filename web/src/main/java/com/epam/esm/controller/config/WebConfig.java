package com.epam.esm.controller.config;

import com.epam.esm.controller.exception.AcceptLocaleResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;

@Configuration
@ComponentScan(basePackages = "com.epam.esm")
public class WebConfig {

    @Bean
    public LocaleResolver sessionLocaleResolver() {
        return new AcceptLocaleResolver();
    }

    @Bean
    public ResourceBundleMessageSource bundleMessageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.addBasenames("errorMessage");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}
