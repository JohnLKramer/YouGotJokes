package dev.johnlkramer.gotjokes.data.entity;

import dev.johnlkramer.gotjokes.Joke;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@Entity
@ToString
@Table(name = "joke")
public class JokeEntity implements PersistableEntity<Joke> {
    public JokeEntity(Joke joke) {
        this.date = joke.getDate();
        this.description = joke.getDescription();
        this.joke = joke.getJoke();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = 0L;

    @Column(name = "joke", nullable = false, unique = false)
    private String joke;

    @Column(name = "description", nullable = true, unique = false)
    private String description;

    @Column(nullable = false, unique = false)
    private LocalDate date;

    public Joke toModel() {
        return new Joke(id, joke, description, date);
    }
}
