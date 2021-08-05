package com.epam.esm.service.config;

import com.epam.esm.service.util.Field;
import com.epam.esm.service.util.SetterStrategy;
import com.epam.esm.service.util.impl.DescriptionSetterStrategy;
import com.epam.esm.service.util.impl.DurationSetterStrategy;
import com.epam.esm.service.util.impl.NameSetterStrategy;
import com.epam.esm.service.util.impl.PriceSetterStrategy;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@EnableTransactionManagement
@Configuration
public class ServiceConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public Map<Field, SetterStrategy> setterMap() {
        List<SetterStrategy> setterStrategies = Arrays.asList(new NameSetterStrategy(), new DescriptionSetterStrategy(), new PriceSetterStrategy(), new DurationSetterStrategy());
        return setterStrategies.stream()
                .collect(Collectors.toMap(SetterStrategy::getField, Function.identity()));
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true)
                .setSkipNullEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);
        return mapper;
    }
}
