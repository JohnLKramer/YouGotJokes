package dev.johnlkramer.gotjokes.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.johnlkramer.gotjokes.Joke;
import dev.johnlkramer.gotjokes.api.JokeApi;
import io.netty.channel.ChannelOption;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

/**
 * Client class implementation of {@code JokeApi}. This will call REST Joke endpoints with the
 * supplied parameters.
 */
public class JokeClient implements JokeApi {

    static final String BASE_PATH = "/jokes";
    static final String DATE_PATH = "/date";
    static final String DATE_PARAM = "date";
    static final String SEARCH_PATH = "/search";
    static final String SEARCH_PARAM = "snippet";
    private static final int DEFAULT_CONNECT_TIMEOUT = 10_000;
    private static final int DEFAULT_RESPONSE_TIMEOUT = 5_000;
    private WebClient webClient;
    private ObjectMapper objectMapper;

    /**
     * Construct client for Joke server running at baseUrl with default timeout values
     * @param baseUrl URL of Joke server
     */
    public JokeClient(String baseUrl) {
        this(baseUrl, DEFAULT_CONNECT_TIMEOUT, DEFAULT_RESPONSE_TIMEOUT);
    }

    /**
     * Construct client for Joke server running at baseUrl with connectTimeoutMs and responseTimeoutMs
     * @param baseUrl URL of Joke server
     * @param connectTimeoutMs
     * @param responseTimeoutMs
     */
    public JokeClient(String baseUrl, int connectTimeoutMs, long responseTimeoutMs) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .clientConnector(httpClientConnector(connectTimeoutMs, responseTimeoutMs))
                .build();
        this.objectMapper = Jackson2ObjectMapperBuilder.json().build();
    }

    private ClientHttpConnector httpClientConnector(int connectTimeoutMs, long responseTimeoutMs) {
        return new ReactorClientHttpConnector(HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeoutMs)
                .responseTimeout(Duration.ofMillis(responseTimeoutMs)));
    }

    /**
     * {@inheritDoc}
     */
    public Joke createJoke(Joke joke) {
        return webClient
                .post()
                .uri(BASE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(joke)
                .retrieve()
                .bodyToMono(Joke.class)
                .block();
    }

    /**
     * {@inheritDoc}
     */
    public List<Joke> getJokesByDate(LocalDate date) {
        return getJokes(DATE_PATH, DATE_PARAM, date);
    }

    /**
     * {@inheritDoc}
     */
    public List<Joke> searchJokes(String snippet) {
        return getJokes(SEARCH_PATH, SEARCH_PARAM, snippet);
    }

    /**
     * {@inheritDoc}
     */
    public List<Joke> getAllJokes() {
        return webClient
                .get()
                .uri(BASE_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Joke>>() {})
                .block();
    }

    private List<Joke> getJokes(String subPath, String paramName, Object paramValue) {
        String path = BASE_PATH + subPath;
        return webClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder.path(path).queryParam(paramName, paramValue).build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Joke>>() {})
                .block();
    }
}
