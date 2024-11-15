package ru.yandex.practicum.filmorate.repository.inDatabase;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

@RequiredArgsConstructor
public class JdbcBaseRepository<T> {

    protected final NamedParameterJdbcOperations jdbc;
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
        } else {
            return null;
        }
    }

    protected <T> int[] batchInsert(String query, Long filmId, List<T> items, Function<T, Long> idExtractor, String idParam) {
        MapSqlParameterSource[] batchParams = new MapSqlParameterSource[items.size()];

        for (int i = 0; i < items.size(); i++) {
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("film_id", filmId);
            params.addValue(idParam, idExtractor.apply(items.get(i))); // Используем переданный параметр idParam для id
            batchParams[i] = params;
        }

        return jdbc.batchUpdate(query, batchParams);
    }
}