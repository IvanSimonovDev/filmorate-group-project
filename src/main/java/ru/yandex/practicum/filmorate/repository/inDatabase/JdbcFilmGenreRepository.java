package ru.yandex.practicum.filmorate.repository.inDatabase;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.ArrayList;
import java.util.Map;

@Slf4j
@Repository
public class JdbcFilmGenreRepository extends JdbcBaseRepository<Object> {

    public JdbcFilmGenreRepository(NamedParameterJdbcOperations jdbc) {
        super(jdbc, null);
    }

    public int[] save(Film film) {
        String sql = """
                     INSERT INTO film_genre (film_id, genre_id)
                     VALUES (:film_id, :genre_id)
                     """;

        return batchInsert(sql,
                film.getId(),
                new ArrayList<>(film.getGenres()),
                Genre::getId, "genre_id"
        );
    }

    public boolean delete(Film film) {
        String sql = """
                     DELETE FROM film_genre
                     WHERE film_id = :film_id
                     """;
        Map<String, Long> params = Map.of("film_id", film.getId());

        return delete(sql, params);
    }

}