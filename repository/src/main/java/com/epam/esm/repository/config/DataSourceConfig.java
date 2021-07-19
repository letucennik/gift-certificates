package com.epam.esm.repository.config;


import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan("com.epam.esm.repository.entity")
@EnableJpaRepositories(basePackages = "com.epam.esm.repository")
public class DataSourceConfig {

}
