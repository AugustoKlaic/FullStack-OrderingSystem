package com.augusto.backend.config;

import com.augusto.backend.domain.BilletPayment;
import com.augusto.backend.domain.CreditCardPayment;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class JacksonConfig {

    @Bean
    public Jackson2ObjectMapperBuilder objectMapperBuilder() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder() {

            @Override
            public void configure(ObjectMapper objectMapper) {
                objectMapper.registerSubtypes(CreditCardPayment.class);
                objectMapper.registerSubtypes(BilletPayment.class);
                super.configure(objectMapper);
            }
        };
        return builder;
    }
}
