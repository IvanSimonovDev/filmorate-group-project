package ru.yandex.practicum.filmorate.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.UserFriend;

import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class UserFriendRepository extends BaseRepository<UserFriend> {
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM user_friend WHERE id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM user_friend";

    public UserFriendRepository(JdbcTemplate jdbc, RowMapper<UserFriend> mapper) {
        super(jdbc, mapper);
    }

    public List<UserFriend> getAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public Optional<UserFriend> getById(long id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }


}
