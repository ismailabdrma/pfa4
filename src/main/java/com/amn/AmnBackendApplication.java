package com.amn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.amn.nosql.repository")
@EnableJpaRepositories(basePackages = "com.amn.repository")
@EntityScan(basePackages = "com.amn.entity")

public class AmnBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(AmnBackendApplication.class, args);
    }
}
