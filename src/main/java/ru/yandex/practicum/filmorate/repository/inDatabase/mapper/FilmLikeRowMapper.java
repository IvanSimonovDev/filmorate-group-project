package ru.yandex.practicum.filmorate.repository.inDatabase.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FilmLike;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FilmLikeRowMapper implements RowMapper<FilmLike> {
    @Override
    public FilmLike mapRow(ResultSet rs, int rowNum) throws SQLException {
        FilmLike filmLike = new FilmLike();
        filmLike.setFilmId(rs.getLong("film_id"));
        filmLike.setUserId(rs.getLong("film_likes_user_id"));
        return filmLike;
    }
}
