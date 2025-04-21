package dev.johnlkramer.gotjokes.data.repo;

import dev.johnlkramer.gotjokes.data.entity.JokeEntity;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JokeRepository extends JpaRepository<JokeEntity, Long> {
    @Query(value = "SELECT * FROM joke j WHERE j.date = :date ORDER BY id ASC", nativeQuery = true)
    List<JokeEntity> findJokesByDate(@Param("date") LocalDate date);

    @Query(
            value =
                    """
        SELECT * FROM joke j WHERE lower(j.joke) LIKE :searchexp
                                OR lower(j.description) LIKE :searchexp ORDER BY DATE ASC, ID ASC
""",
            nativeQuery = true)
    List<JokeEntity> searchJokes(@Param("searchexp") String searchexp);

    static String searchExpression(String snippet) {
        return "%" + snippet.toLowerCase() + "%";
    }
}
