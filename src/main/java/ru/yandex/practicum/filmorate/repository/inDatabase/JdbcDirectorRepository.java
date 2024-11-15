package ru.yandex.practicum.filmorate.repository.inDatabase;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.repository.DirectorRepository;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Primary
@Repository
public class JdbcDirectorRepository extends JdbcBaseRepository<Director> implements DirectorRepository {

    public JdbcDirectorRepository(NamedParameterJdbcOperations jdbc, RowMapper<Director> mapper) {
        super(jdbc, mapper);
    }

    public List<Director> getAll() {
        String sql = """
                     SELECT * FROM directors
                     """;

        return findMany(sql, Map.of());
    }

    public Optional<Director> getById(long id) {
        String sql = """
                     SELECT * FROM directors WHERE id = :id
                     """;
        Map<String, Long> params = Map.of("id", id);

        return findOne(sql, params);
    }

    public List<Director> getByIds(List<Long> ids) {
        String list = ids.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        String sqlTemplate = """
                             SELECT * FROM directors WHERE id in ( %s )
                             """;
        String sql = String.format(sqlTemplate, list);

        return findMany(sql, Collections.emptyMap());
    }

    public Director save(Director director) {
        String sql = """
                     INSERT INTO directors (name) VALUES (:name)
                     """;
        Map<String, Object> params = Map.of("name", director.getName());

        long id = insert(sql, params);
        director.setId(id);

        return director;
    }

    public Director update(Director director) {
        String sql = """
                     UPDATE directors SET name = :name WHERE id = :id
                     """;
        Map<String, Object> params = Map.of("name", director.getName(), "id", director.getId());
        update(sql, params);

        return director;
    }

    public void delete(final long id) {
        String sql = """
                     DELETE from directors WHERE id = :id
                     """;
        Map<String, Object> params = Map.of("id", id);

        delete(sql, params);
    }

    public List<Director> getAllDirectorsByFilmId(long filmId) {
        String sql = """
                     SELECT d.*
                     FROM directors AS d
                     JOIN film_director AS fd ON d.id = fd.director_id
                     WHERE fd.film_id = :id
                     GROUP BY d.id
                     ORDER BY d.id ASC
                     """;
        return findMany(sql, Map.of("id", filmId));
    }
}