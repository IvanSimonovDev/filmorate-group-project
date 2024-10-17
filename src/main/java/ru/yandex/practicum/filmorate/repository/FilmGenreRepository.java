package ru.yandex.practicum.filmorate.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class FilmGenreRepository  extends BaseRepository<FilmGenre> {

    private static final String FIND_BY_ID_QUERY = "SELECT * FROM film_genre WHERE id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM film_genre";

    public FilmGenreRepository(JdbcTemplate jdbc, RowMapper<FilmGenre> mapper) {
        super(jdbc, mapper);
    }

    public List<FilmGenre> getAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public Optional<FilmGenre> getById(long id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

}
