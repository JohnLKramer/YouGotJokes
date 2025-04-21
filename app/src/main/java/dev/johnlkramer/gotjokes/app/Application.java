package dev.johnlkramer.gotjokes.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "dev.johnlkramer.gotjokes")
@EnableJpaRepositories(basePackages = "dev.johnlkramer.gotjokes.data.repo")
@EntityScan(basePackages = "dev.johnlkramer.gotjokes.data.entity")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
