package ru.yandex.practicum.filmorate.repository.inDatabase.mapper;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Set;

@Component
public class FilmExtractor implements ResultSetExtractor<Film> {

    @Override
    public Film extractData(ResultSet rs) throws SQLException, DataAccessException {
        Set<Genre> genres = new LinkedHashSet<>();
        Set<Director> directors = new LinkedHashSet<>();
        Film film = null;

        while (rs.next()) {
            if (film == null) {
                film = new Film();
                film.setId(rs.getLong("id"));
                film.setName(rs.getString("name"));
                film.setDescription(rs.getString("description"));
                film.setReleaseDate(rs.getDate("release_date").toLocalDate());
                film.setDuration(rs.getInt("duration"));
                film.setMpa(new Mpa(rs.getLong("mpa.id"), rs.getString("mpa.name")));
            }
            long genreId = rs.getLong("genre.id");
            if (!rs.wasNull()) {
                genres.add(new Genre(genreId, rs.getString("genre.name")));
            }
            long directorId = rs.getLong("directors.id");
            if (!rs.wasNull()) {
                directors.add(new Director(directorId, rs.getString("directors.name")));
            }
        }

        if (!genres.isEmpty()) {
            film.setGenres(genres);
        }
        if (!directors.isEmpty() || film != null) {
            film.setDirectors(directors);
        }

        return film;
    }
}
