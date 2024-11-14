package ru.yandex.practicum.filmorate.repository.inDatabase;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.Map;

@Slf4j
@Repository
public class JdbcFilmDirectorRepository extends JdbcBaseRepository<Object> {

    public JdbcFilmDirectorRepository(NamedParameterJdbcOperations jdbc) {
        super(jdbc, null);
    }

    public int[] save(Film film) {
        String sql = "INSERT INTO film_director (film_id, director_id) VALUES (:film_id, :director_id)";

        return batchInsert(sql,
                film.getId(),
                new ArrayList<>(film.getDirectors()),
                Director::getId, "director_id"
        );
    }

    public boolean delete(Film film) {
        String sql = "delete from film_director where film_id = :film_id";
        Map<String, Long> params = Map.of("film_id", film.getId());

        return delete(sql, params);
    }

}