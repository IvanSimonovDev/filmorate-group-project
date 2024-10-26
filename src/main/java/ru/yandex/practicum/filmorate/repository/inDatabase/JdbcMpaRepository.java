package ru.yandex.practicum.filmorate.repository.inDatabase;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.MpaRepository;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@Slf4j
public class JdbcMpaRepository extends JdbcBaseRepository<Mpa> implements MpaRepository {

//    private static final String FIND_BY_ID_QUERY = "SELECT * FROM mpa WHERE id = ?";
//    private static final String FIND_ALL_QUERY = "SELECT * FROM mpa";

    public JdbcMpaRepository(NamedParameterJdbcOperations jdbc, RowMapper<Mpa> mapper) {
        super(jdbc, mapper);
    }

    public List<Mpa> getAll() {
        String sql = "SELECT * FROM mpa";
       return findMany(sql, Collections.emptyMap());
    }

    public Optional<Mpa> getById(long id) {
        String sql = "SELECT * FROM mpa WHERE id = :id";
        Map<String, Long> params = Map.of("id", id);
        return findOne(sql, params);
    }


}
