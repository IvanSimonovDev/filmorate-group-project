package ru.yandex.practicum.filmorate.repository.inDatabase;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FilmDirectorRowMapper implements RowMapper<JdbcFilmRepository.FilmDirector> {
    @Override
    public JdbcFilmRepository.FilmDirector mapRow(ResultSet rs, int rowNum) throws SQLException {
        long filmId = rs.getLong("film_id");
        long directorId = rs.getLong("director_id");
        return new JdbcFilmRepository.FilmDirector(filmId, directorId);
    }

}