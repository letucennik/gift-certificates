package com.epam.esm.config;

import com.epam.esm.util.Field;
import com.epam.esm.util.SetterStrategy;
import com.epam.esm.util.impl.DescriptionSetterStrategy;
import com.epam.esm.util.impl.DurationSetterStrategy;
import com.epam.esm.util.impl.NameSetterStrategy;
import com.epam.esm.util.impl.PriceSetterStrategy;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@EnableTransactionManagement
@org.springframework.context.annotation.Configuration
public class ServiceConfig {

    @Bean
    public Map<Field, SetterStrategy> setterMap(){
        List<SetterStrategy> setterStrategies= Arrays.asList(new NameSetterStrategy(), new DescriptionSetterStrategy(), new PriceSetterStrategy(), new DurationSetterStrategy());
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
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);
        return mapper;
    }
}
