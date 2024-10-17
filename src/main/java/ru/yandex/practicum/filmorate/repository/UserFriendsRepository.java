package ru.yandex.practicum.filmorate.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.UserFriends;

import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class UserFriendsRepository extends BaseRepository<UserFriends> {
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM mpa WHERE id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM mpa";

    public UserFriendsRepository(JdbcTemplate jdbc, RowMapper<UserFriends> mapper) {
        super(jdbc, mapper);
    }

    public List<UserFriends> getAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public Optional<UserFriends> getById(long id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }


}
