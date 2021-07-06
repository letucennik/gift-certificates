package com.epam.esm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
@ComponentScan(basePackages = "com.epam.esm")
public class WebConfig {

    @Bean
    public ResourceBundleMessageSource getResourceBundleMessageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.addBasenames("errorMessage");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}
