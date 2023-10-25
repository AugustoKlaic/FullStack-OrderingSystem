package com.augusto.backend.config;

import com.augusto.backend.config.service.CreateTestDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.text.ParseException;

@Configuration
@Profile("dev")
public class ProfileDevConfigurer {
    private static final String CREATE_STRATEGY = "create";

    private final CreateTestDatabase createTestDatabase;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String databaseCreationStrategy;

    @Autowired
    public ProfileDevConfigurer(CreateTestDatabase createTestDatabase) {
        this.createTestDatabase = createTestDatabase;
    }

    @Bean
    public boolean instantiateDatabase() throws ParseException {

        if(!CREATE_STRATEGY.equals(databaseCreationStrategy)){
            return false;
        }

        createTestDatabase.instantiateTestDatabase();
        return true;
    }
}
