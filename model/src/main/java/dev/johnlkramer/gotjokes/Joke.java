package dev.johnlkramer.gotjokes;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Joke Model Object - POJO for passing to and from JokeApi
 */
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Joke implements ModelType {
    /**
     * Joke id. If this is specified by API consumers on create, it will be ignored.
     */
    @JsonProperty("id")
    private Long id;

    /**
     * Any joke you please (required)
     */
    @NotNull
    @Nonnull
    @JsonProperty("joke")
    private String joke;

    /**
     * Description (optional)
     */
    @JsonProperty("description")
    private String description;

    /**
     * Date of joke
     */
    @NotNull
    @Nonnull
    @JsonProperty("date")
    private LocalDate date;

    public Joke(@Nonnull String joke, String description, @Nonnull LocalDate date) {
        this.joke = joke;
        this.description = description;
        this.date = date;
    }
}
