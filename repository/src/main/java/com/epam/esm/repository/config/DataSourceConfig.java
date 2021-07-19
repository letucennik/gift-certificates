package com.epam.esm.repository.config;


import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EntityScan("com.epam.esm.repository.entity")
public class DataSourceConfig {

}
