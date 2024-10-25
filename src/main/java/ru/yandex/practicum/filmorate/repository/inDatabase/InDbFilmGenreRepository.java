package ru.yandex.practicum.filmorate.repository.inDatabase;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class InDbFilmGenreRepository extends InDbBaseRepository<FilmGenre> {

//    private static final String FIND_BY_ID_QUERY = "SELECT * FROM film_genre WHERE film_id = ?";
//    private static final String FIND_ALL_QUERY = "SELECT * FROM film_genre";
//    private static final String INSERT_QUERY = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)";

    public InDbFilmGenreRepository(JdbcTemplate jdbc, RowMapper<FilmGenre> mapper) {
        super(jdbc, mapper);
    }

    public List<FilmGenre> getAll() {
        String sql = "SELECT * FROM film_genre";
        return findMany(sql);
    }

    public List<FilmGenre> getById(long id) {
        String sql = "SELECT * FROM film_genre WHERE film_id = ?";
        return findMany(sql, id);
    }

    public int[] save(Film film) {
        String sql = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)";
        return batchInsert(sql,
                film.getId(),
                new ArrayList<>(film.getGenres())
        );
    }

    public boolean delete(Film film) {
        String sql ="delete from film_genre where film_id = ?";
        return delete(sql, film.getId()
        );
    }

}
