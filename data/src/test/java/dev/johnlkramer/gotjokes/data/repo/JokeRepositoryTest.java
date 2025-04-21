package dev.johnlkramer.gotjokes.data.repo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.johnlkramer.gotjokes.Joke;
import dev.johnlkramer.gotjokes.data.entity.JokeEntity;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(
        properties = {
            "spring.datasource.url=jdbc:postgresql://localhost:5433/joketest",
            "spring.datasource.username=pguser",
            "spring.datasource.password=pgpass",
            "spring.jpa.hibernate.ddl-auto=create-drop",
            "spring.jpa.show-sql=true",
            "spring.jpa.properties.hibernate.format_sql=true",
            "logging.level.org.hibernate.SQL=DEBUG",
            "logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE",
        })
@DataJpaTest
@ContextConfiguration(classes = JokeTestConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class JokeRepositoryTest {

    @Autowired
    private JokeRepository jokeRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setUp() {
        jdbcTemplate.execute("TRUNCATE TABLE joke RESTART IDENTITY");
    }

    @Test
    public void test_create_and_retrieve_joke() {
        String jokeStr = "I have an inferiority complex, but it's not a very good one.";
        String description = "LOL";
        LocalDate today = LocalDate.now();
        Joke joke = new Joke(jokeStr, description, today);
        JokeEntity jokeEntity = new JokeEntity(joke);

        JokeEntity savedJokeEntity = jokeRepository.save(jokeEntity);

        Optional<JokeEntity> retrievedJoke = jokeRepository.findById(savedJokeEntity.getId());

        assertTrue(retrievedJoke.isPresent());
        assertEquals(jokeStr, retrievedJoke.get().getJoke());
        assertEquals(description, retrievedJoke.get().getDescription());
        assertEquals(today, retrievedJoke.get().getDate());
    }

    @Test
    public void test_find_jokes_by_date() {

        //  I saw a sign that said "watch for children" and I thought, "That sounds like a fair trade"
        // A photon walks into a hotel. The bellhop asks if he needs help with his bags. The photon replies, "No thanks,
        // I'm travelling light."
        String infJokeStr = "I have an inferiority complex, but it's not a very good one.";
        String infDescription = "LOL";
        LocalDate today = LocalDate.now();
        Joke infJoke = new Joke(infJokeStr, infDescription, today);
        JokeEntity infJokeEntity = new JokeEntity(infJoke);

        jokeRepository.save(infJokeEntity);

        String watchJoke =
                "I saw a sign that said \"watch for children\" and I thought, \"That sounds like a fair trade\"";
        JokeEntity watchJokeEntity = new JokeEntity(new Joke(watchJoke, null, today));
        jokeRepository.save(watchJokeEntity);

        String photonJoke =
                "A photon walks into a hotel. The bellhop asks if he needs help with his bags. The photon replies, \"No thanks, I'm travelling light.\"";
        LocalDate yesterday = today.minusDays(1);
        JokeEntity photonJokeEntity = new JokeEntity(new Joke(photonJoke, null, yesterday));

        jokeRepository.save(photonJokeEntity);

        List<JokeEntity> todaysJokes = jokeRepository.findJokesByDate(today);

        System.out.println(todaysJokes);

        assertEquals(2, todaysJokes.size());
        JokeEntity firstJoke = todaysJokes.get(0);

        assertEquals(infJokeStr, firstJoke.getJoke());
        assertEquals(infDescription, firstJoke.getDescription());
        assertEquals(today, firstJoke.getDate());

        JokeEntity secondJoke = todaysJokes.get(1);
        assertEquals(watchJoke, secondJoke.getJoke());
        assertNull(secondJoke.getDescription());
        assertEquals(today, secondJoke.getDate());

        List<JokeEntity> allJokes = jokeRepository.findAll();

        assertEquals(3, allJokes.size());
        assertEquals(
                Set.of(infJokeStr, watchJoke, photonJoke),
                allJokes.stream().map(je -> je.getJoke()).collect(Collectors.toSet()));
    }
}
