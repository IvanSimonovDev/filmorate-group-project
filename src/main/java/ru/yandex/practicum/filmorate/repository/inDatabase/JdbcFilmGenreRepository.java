package ru.yandex.practicum.filmorate.repository.inDatabase;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.Map;

@Repository
@Slf4j
public class JdbcFilmGenreRepository extends JdbcBaseRepository<Object> {

    public JdbcFilmGenreRepository(NamedParameterJdbcOperations jdbc) {
        super(jdbc, null);
    }

    public int[] save(Film film) {
        String sql = "INSERT INTO film_genre (film_id, genre_id) VALUES (:film_id, :genre_id)";
//        Map<String, Object> params = Map.of("film_id", film.getId(), "genre_id", new ArrayList<>(film.getGenres()));

        return batchInsert(sql,
                film.getId(),
                new ArrayList<>(film.getGenres())
        );
    }

    public boolean delete(Film film) {
        String sql = "delete from film_genre where film_id = :film_id";
        Map<String, Long> params = Map.of("film_id", film.getId());

        return delete(sql, params);
    }

}