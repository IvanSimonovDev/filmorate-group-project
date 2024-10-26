package ru.yandex.practicum.filmorate.repository.inDatabase;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Map;

@Repository
@Slf4j
public class JdbcUserFriendRepository extends JdbcBaseRepository<Object> {
//    private static final String INSERT_FRIEND_QUERY = "INSERT INTO user_friend (user_id, friend_id, isConfirmed) VALUES (?, ?, ?)";
//    private static final String DELETE_FRIEND_QUERY = "DELETE from user_friend WHERE  user_id = ? AND friend_id = ?";

    public JdbcUserFriendRepository(NamedParameterJdbcOperations jdbc) {
        super(jdbc, null);
    }

    public void add(User user, User friend, boolean isConfirmed) {
        String sql = "INSERT INTO user_friend (user_id, friend_id, isConfirmed) VALUES (:user_id, :friend_id, :isConfirmed)";
        Map<String, Object> params = Map.of("user_id", user.getId(), "friend_id", friend.getId(), "isConfirmed", isConfirmed);

        insert(sql, params
//                user.getId(),
//                friend.getId(),
//                isConfirmed
        );
    }

    public void delete(User user, User friend) {
        String sql = "DELETE from user_friend WHERE  user_id = :user_Id AND friend_id = :friend_id";
        Map<String, Long> params = Map.of("user_Id", user.getId(), "friend_id",friend.getId());

        delete(sql, params);
    }

}