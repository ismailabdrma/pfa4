package com.amn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class AmnBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(AmnBackendApplication.class, args);
    }

}
