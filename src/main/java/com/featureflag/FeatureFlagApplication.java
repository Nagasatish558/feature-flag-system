package com.featureflag;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class FeatureFlagApplication {

    public static void main(String[] args) {
        // Disable Liquibase secure parsing to allow remote XSD lookups
        System.setProperty("liquibase.secureParsing", "false");
        SpringApplication.run(FeatureFlagApplication.class, args);
    }
}
