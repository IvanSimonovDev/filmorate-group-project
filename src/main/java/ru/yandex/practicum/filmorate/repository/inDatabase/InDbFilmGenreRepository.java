package ru.yandex.practicum.filmorate.repository.inDatabase;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;

@Repository
@Slf4j
public class InDbFilmGenreRepository extends InDbBaseRepository<Object> {

    public InDbFilmGenreRepository(JdbcTemplate jdbc) {
        super(jdbc, null);
    }

    public int[] save(Film film) {
        String sql = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)";
        return batchInsert(sql,
                film.getId(),
                new ArrayList<>(film.getGenres())
        );
    }

    public boolean delete(Film film) {
        String sql = "delete from film_genre where film_id = ?";
        return delete(sql, film.getId()
        );
    }

}