package com.tavolo.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TavoloBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(TavoloBackendApplication.class, args);
    }

}
