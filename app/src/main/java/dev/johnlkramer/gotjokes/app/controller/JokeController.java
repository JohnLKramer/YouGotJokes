package dev.johnlkramer.gotjokes.app.controller;

import dev.johnlkramer.gotjokes.Joke;
import dev.johnlkramer.gotjokes.api.JokeApi;
import dev.johnlkramer.gotjokes.service.JokeService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Service implementation of JokeApi
 */
@RestController
@Validated
@RequestMapping("/jokes")
public class JokeController implements JokeApi {
    private JokeService jokeService;

    public JokeController(JokeService jokeService) {
        this.jokeService = jokeService;
    }

    /**
     * {@inheritDoc}
     */
    @PostMapping
    public Joke createJoke(@Valid @NotNull @RequestBody Joke joke) {
        return jokeService.createJoke(joke);
    }

    /**
     * {@inheritDoc}
     */
    @Operation(summary = "Get jokes by date")
    @GetMapping(path = "/date", params = "date")
    public List<Joke> getJokesByDate(@RequestParam LocalDate date) {
        return jokeService.getJokesByDate(date);
    }

    /**
     * {@inheritDoc}
     */
    @Operation(summary = "Search joke text")
    @GetMapping(path = "/search", params = "snippet")
    public List<Joke> searchJokes(@RequestParam String snippet) {
        return jokeService.searchJokes(snippet);
    }

    /**
     * {@inheritDoc}
     */
    @Operation(summary = "Get all jokes")
    @GetMapping
    public List<Joke> getAllJokes() {
        return jokeService.getAllJokes();
    }
}
