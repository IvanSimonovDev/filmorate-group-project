package ru.yandex.practicum.filmorate.repository.inDatabase;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

@Repository
@Slf4j
public class InDbUserFriendRepository extends InDbBaseRepository<Object> {
    private static final String INSERT_FRIEND_QUERY = "INSERT INTO user_friend (user_id, friend_id, isConfirmed) VALUES (?, ?, ?)";
    private static final String DELETE_FRIEND_QUERY = "DELETE from user_friend WHERE  user_id = ? AND friend_id = ?";

    public InDbUserFriendRepository(JdbcTemplate jdbc) {
        super(jdbc, null);
    }

    public void add(User user, User friend, boolean isConfirmed) {
        insert(INSERT_FRIEND_QUERY,
                user.getId(),
                friend.getId(),
                isConfirmed
        );
    }

    public void delete(User user, User friend) {
        delete(DELETE_FRIEND_QUERY,
                user.getId(),
                friend.getId()
        );
    }

}