package dev.johnlkramer.gotjokes.app.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.johnlkramer.gotjokes.Joke;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(
        properties = {
            "spring.datasource.url=jdbc:postgresql://localhost:5433/joketest",
            "spring.datasource.username=pguser",
            "spring.datasource.password=pgpass",
            "spring.jpa.hibernate.ddl-auto=none",
            "spring.liquibase.enabled=true",
            "logging.level.org.hibernate.SQL=DEBUG",
            "logging.level.org.hibernate.type.descriptor.sql=TRACE"
        })
public class JokeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setUp() {
        jdbcTemplate.execute("TRUNCATE TABLE joke RESTART IDENTITY");
    }

    @Test
    void test_create_and_get_joke_by_date() throws Exception {
        String jokeStr = "Why can’t you trust an atom? Because they make up literally everything.";
        LocalDate today = LocalDate.now();

        Joke joke = new Joke(jokeStr, null, today);
        mockMvc.perform(post("/jokes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(joke)))
                .andExpect(status().isOk());

        String responseString = mockMvc.perform(get("/jokes").param("date", today.toString()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<Joke> jokes = objectMapper.readValue(responseString, new TypeReference<>() {});

        assertEquals(1, jokes.size());
    }
}
