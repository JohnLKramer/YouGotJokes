package dev.johnlkramer.gotjokes.client;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.johnlkramer.gotjokes.Joke;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.MediaType;
import org.mockserver.model.Parameter;
import org.mockserver.socket.PortFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

public class JokeClientTest {
    private int port;
    private ClientAndServer mockServer;
    private MockServerClient mockServerClient;
    private ObjectMapper objectMapper;
    private JokeClient jokeClient;

    @BeforeEach
    public void setUp() {
        port = PortFactory.findFreePort();
        mockServer = ClientAndServer.startClientAndServer(port);
        mockServerClient = new MockServerClient("localhost", port);
        objectMapper = Jackson2ObjectMapperBuilder.json().build();
        jokeClient = new JokeClient("http://localhost:" + port);
    }

    @AfterEach
    public void tearDown() {
        mockServer.stop();
    }

    @Test
    public void test_create_joke() throws JsonProcessingException {
        Joke joke = joke();
        mockServerClient
                .when(HttpRequest.request().withMethod(HttpMethod.POST.name()).withPath(JokeClient.BASE_PATH))
                .respond(HttpResponse.response()
                        .withContentType(MediaType.APPLICATION_JSON)
                        .withBody(objectMapper.writeValueAsString(joke)) // the service assigns the id
                        .withStatusCode(200));

        Joke jokeResponse = jokeClient.createJoke(joke);
        assertEquals(joke.getJoke(), jokeResponse.getJoke());
        assertEquals(joke.getDescription(), jokeResponse.getDescription());
        assertEquals(joke.getDate(), jokeResponse.getDate());
        mockServerClient.verify(HttpRequest.request(JokeClient.BASE_PATH).withMethod(HttpMethod.POST.name()));
    }

    @Test
    public void test_get_all_jokes() throws JsonProcessingException {
        Joke joke = joke();
        List<Joke> jokeList = List.of(joke);
        mockServerClient
                .when(HttpRequest.request().withMethod(HttpMethod.GET.name()).withPath(JokeClient.BASE_PATH))
                .respond(HttpResponse.response()
                        .withContentType(MediaType.APPLICATION_JSON)
                        .withBody(objectMapper.writeValueAsString(jokeList))
                        .withStatusCode(200));

        List<Joke> jokeResponse = jokeClient.getAllJokes();
        assertEquals(1, jokeResponse.size());
        assertEquals(joke.getJoke(), jokeResponse.get(0).getJoke());
        assertEquals(joke.getDescription(), jokeResponse.get(0).getDescription());
        assertEquals(joke.getDate(), jokeResponse.get(0).getDate());
        mockServerClient.verify(HttpRequest.request(JokeClient.BASE_PATH).withMethod(HttpMethod.GET.name()));
    }

    @Test
    public void test_get_jokes_by_date() throws JsonProcessingException {
        Joke joke = joke();
        List<Joke> jokeList = List.of(joke);
        mockServerClient
                .when(HttpRequest.request()
                        .withMethod(HttpMethod.GET.name())
                        .withPath(JokeClient.BASE_PATH + JokeClient.DATE_PATH)
                        .withQueryStringParameter(new Parameter(
                                JokeClient.DATE_PARAM, joke.getDate().toString())))
                .respond(HttpResponse.response()
                        .withContentType(MediaType.APPLICATION_JSON)
                        .withBody(objectMapper.writeValueAsString(jokeList))
                        .withStatusCode(200));

        List<Joke> jokeResponse = jokeClient.getJokesByDate(joke.getDate());
        assertEquals(1, jokeResponse.size());
        assertEquals(joke.getJoke(), jokeResponse.get(0).getJoke());
        assertEquals(joke.getDescription(), jokeResponse.get(0).getDescription());
        assertEquals(joke.getDate(), jokeResponse.get(0).getDate());
        mockServerClient.verify(HttpRequest.request(JokeClient.BASE_PATH + JokeClient.DATE_PATH)
                .withMethod(HttpMethod.GET.name())
                .withQueryStringParameter(JokeClient.DATE_PARAM, joke.getDate().toString()));
    }

    @Test
    public void test_search_jokes() throws JsonProcessingException {
        Joke joke = joke();
        List<Joke> jokeList = List.of(joke);
        mockServerClient
                .when(HttpRequest.request()
                        .withMethod(HttpMethod.GET.name())
                        .withPath(JokeClient.BASE_PATH + JokeClient.SEARCH_PATH))
                .respond(HttpResponse.response()
                        .withContentType(MediaType.APPLICATION_JSON)
                        .withBody(objectMapper.writeValueAsString(jokeList))
                        .withStatusCode(200));

        List<Joke> jokeResponse = jokeClient.searchJokes(joke.getJoke().substring(0, 10));
        assertEquals(1, jokeResponse.size());
        assertEquals(joke.getJoke(), jokeResponse.get(0).getJoke());
        assertEquals(joke.getDescription(), jokeResponse.get(0).getDescription());
        assertEquals(joke.getDate(), jokeResponse.get(0).getDate());
        mockServerClient.verify(HttpRequest.request(JokeClient.BASE_PATH + JokeClient.SEARCH_PATH)
                .withMethod(HttpMethod.GET.name())
                .withQueryStringParameter(
                        JokeClient.SEARCH_PARAM, joke.getJoke().substring(0, 10)));
    }

    private Joke joke() {
        String jokeStr =
                "A man walked into his house and was delighted when he discovered that someone had stolen all of his lamps.";
        String description = "De-lighted";
        LocalDate today = LocalDate.now();
        return new Joke(jokeStr, description, today);
    }
}
