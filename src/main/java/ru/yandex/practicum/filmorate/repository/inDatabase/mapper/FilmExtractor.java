package ru.yandex.practicum.filmorate.repository.inDatabase.mapper;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
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
            genres.add(new Genre(rs.getLong("genre.id"), rs.getString("genre.name")));
        }
        if (film != null) {
            film.setGenres(genres);
        }
        return film;
    }

}
