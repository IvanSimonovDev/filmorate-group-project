package ru.yandex.practicum.filmorate.repository.inDatabase;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FilmGenreRowMapper implements RowMapper<InDbFilmRepository.FilmGenre> {
    @Override
    public InDbFilmRepository.FilmGenre mapRow(ResultSet rs, int rowNum) throws SQLException {
        long filmId = rs.getLong("film_id");
        long genreId = rs.getLong("genre_id");
        return new InDbFilmRepository.FilmGenre(filmId, genreId);
    }

}