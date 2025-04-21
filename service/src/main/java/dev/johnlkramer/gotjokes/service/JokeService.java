package dev.johnlkramer.gotjokes.service;

import static dev.johnlkramer.gotjokes.data.entity.PersistableEntity.entitiesToModel;
import static dev.johnlkramer.gotjokes.data.repo.JokeRepository.searchExpression;

import dev.johnlkramer.gotjokes.Joke;
import dev.johnlkramer.gotjokes.api.JokeApi;
import dev.johnlkramer.gotjokes.data.entity.JokeEntity;
import dev.johnlkramer.gotjokes.data.repo.JokeRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Implementation of JokeApi as Injectable Bean
 */
@Service
public class JokeService implements JokeApi {
    public JokeService(JokeRepository jokeRepository) {
        this.jokeRepository = jokeRepository;
    }

    private JokeRepository jokeRepository;

    /**
     * {@inheritDoc}
     */
    public Joke createJoke(Joke joke) {
        JokeEntity jokeEntity = new JokeEntity(joke);
        return jokeRepository.saveAndFlush(jokeEntity).toModel();
    }

    /**
     * {@inheritDoc}
     */
    public List<Joke> getJokesByDate(LocalDate date) {
        return entitiesToModel(jokeRepository.findJokesByDate(date));
    }

    /**
     * {@inheritDoc}
     */
    public List<Joke> getAllJokes() {
        return entitiesToModel(jokeRepository.findAll());
    }

    /**
     * {@inheritDoc}
     */
    public List<Joke> searchJokes(String snippet) {
        return entitiesToModel(jokeRepository.searchJokes(searchExpression(snippet)));
    }
}
