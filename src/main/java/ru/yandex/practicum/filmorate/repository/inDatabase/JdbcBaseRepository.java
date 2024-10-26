package ru.yandex.practicum.filmorate.repository.inDatabase;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
public class JdbcBaseRepository<T> {

    protected final NamedParameterJdbcOperations jdbc;
     //JdbcTemplate jdbc;
    protected final RowMapper<T> mapper;


    protected Optional<T> findOne(String query, Map<String, ?> params) {
        try {
            T result = jdbc.queryForObject(query, params, mapper);
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }

    protected List<T> findMany(String query, Map<String, ?> params) {
        return jdbc.query(query, params, mapper);
    }

    protected boolean delete(String query, Map<String, ?> params) {
        int rowsDeleted = jdbc.update(query, params);
        return rowsDeleted > 0;
    }

    protected void update(String query, Map<String, ?> params) {
        int rowsUpdated = jdbc.update(query, params);
        if (rowsUpdated == 0) {
            throw new ValidationException("Не удалось обновить данные с: " + params);
        }
    }

    protected Long insert(String query, Map<String, ?> params) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource(params);
        jdbc.update(query, sqlParameterSource, keyHolder);

        Long id = null;
        if (keyHolder.getKeys() != null && !keyHolder.getKeys().isEmpty() && keyHolder.getKeys().size() == 1) {
            id = Objects.requireNonNull(keyHolder.getKeyAs(Integer.class)).longValue();
        }

        // Возвращаем id нового пользователя
        if (id != null) {
            return id;
        } else if (keyHolder.getKeys().size() > 1) {
            return null;
        } else {
            throw new ValidationException("Не удалось сохранить данные: " + params);
        }
    }

    protected int[] batchInsert(String query, Long filmId, List<Genre> genres) {
        MapSqlParameterSource[] batchParams = new MapSqlParameterSource[genres.size()];

        for (int i = 0; i < genres.size(); i++) {
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("film_id", filmId);
            params.addValue("genre_id", genres.get(i).getId());
            batchParams[i] = params;
        }

        return jdbc.batchUpdate(query, batchParams
//                new BatchPreparedStatementSetter() {
//                    @Override
//                    public void setValues(PreparedStatement ps, int i) throws SQLException {
//                        ps.setLong(1, filmId);
//                        ps.setLong(2, params.get(i).getId());
//                    }
//
//                    @Override
//                    public int getBatchSize() {
//                        return params.size();
//                    }
//                }
                );
    }

}