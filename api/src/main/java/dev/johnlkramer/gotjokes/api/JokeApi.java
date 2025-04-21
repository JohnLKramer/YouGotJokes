package dev.johnlkramer.gotjokes.api;

import dev.johnlkramer.gotjokes.Joke;
import jakarta.annotation.Nonnull;
import java.time.LocalDate;
import java.util.List;

/**
 * CRUD Interface for storing and retrieving jokes.
 */
public interface JokeApi {

    /**
     * Create Joke
     * @param joke
     * @return the joke created
     */
    @Nonnull
    Joke createJoke(@Nonnull Joke joke);

    /**
     * Get all the jokes for a given date
     * @param date
     * @return List of jokes for a given date
     */
    @Nonnull
    List<Joke> getJokesByDate(LocalDate date);

    /**
     * Get all jokes for which the supplied snippet is a substring
     * @param snippet
     * @return List of jokes with snippet as substring
     */
    @Nonnull
    List<Joke> searchJokes(String snippet);

    /**
     * Get all jokes
     * @return List of all jokes
     */
    @Nonnull
    List<Joke> getAllJokes();
}
