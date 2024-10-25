package ru.yandex.practicum.filmorate.repository.inDatabase;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class InDbMpaRepository extends InDbBaseRepository<Mpa> {

//    private static final String FIND_BY_ID_QUERY = "SELECT * FROM mpa WHERE id = ?";
//    private static final String FIND_ALL_QUERY = "SELECT * FROM mpa";

    public InDbMpaRepository(JdbcTemplate jdbc, RowMapper<Mpa> mapper) {
        super(jdbc, mapper);
    }

    public List<Mpa> getAll() {
        String sql = "SELECT * FROM mpa";
       return findMany(sql);
    }

    public Optional<Mpa> getById(long id) {
        String sql = "SELECT * FROM mpa WHERE id = ?";
        return findOne(sql, id);
    }


}
