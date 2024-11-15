package ru.yandex.practicum.filmorate.repository.inDatabase;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.GenreRepository;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class JdbcGenreRepository extends JdbcBaseRepository<Genre> implements GenreRepository {

    public JdbcGenreRepository(NamedParameterJdbcOperations jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    public List<Genre> getAll() {
        String sql = """
                     SELECT * FROM genre
                     """;
        return findMany(sql, Collections.emptyMap());
    }

    public Optional<Genre> getById(long id) {
        String sql = """
                     SELECT * FROM genre WHERE id = :id
                     """;
        Map<String, Long> params = Map.of("id", id);
        return findOne(sql, params);
    }

    public List<Genre> getByIds(List<Long> ids) {
        String list = ids.stream().map(String::valueOf)
                .collect(Collectors.joining(","));
        String sqlTemplate = """
                             SELECT * FROM genre
                             WHERE id in (%s)
                             """;
        String sql = String.format(sqlTemplate, list);

        return findMany(sql, Collections.emptyMap());
    }

    public List<Genre> getAllGenresByFilmId(long filmId) {
        String sql = """
                     SELECT g.*
                     FROM genre AS g
                     JOIN film_genre AS fg ON g.id = fg.genre_id
                     WHERE fg.film_id = :id
                     GROUP BY g.id
                     ORDER BY g.id ASC
                     """;
        return findMany(sql, Map.of("id", filmId));
    }
}