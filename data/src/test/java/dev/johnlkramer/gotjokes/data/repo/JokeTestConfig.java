package dev.johnlkramer.gotjokes.data.repo;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
// @ComponentScan(basePackages = "dev.johnlkramer.gotjokes.data")
@EnableJpaRepositories(basePackages = "dev.johnlkramer.gotjokes.data.repo")
@EntityScan(basePackages = "dev.johnlkramer.gotjokes.data.entity")
public class JokeTestConfig {}
