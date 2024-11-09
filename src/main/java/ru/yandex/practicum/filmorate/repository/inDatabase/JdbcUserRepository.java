package ru.yandex.practicum.filmorate.repository.inDatabase;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.util.*;

@Primary
@Repository
public class JdbcUserRepository extends JdbcBaseRepository<User> implements UserRepository {

    public JdbcUserRepository(NamedParameterJdbcOperations jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    public Optional<User> get(long userId) {
        String sql = "SELECT * FROM users WHERE id = :userId";
        Map<String, Long> params = Map.of("userId", userId);
        return findOne(sql, params);
    }

    public List<User> getAll() {
        String sql = "SELECT * FROM users";

        return findMany(sql, Map.of());
    }

    public User save(User user) {
        String sql = "INSERT INTO users (email, login, name, birthday)" +
                "VALUES (:email, :login, :name, :birthday)";
        Map<String, Object> params = Map.of("email", user.getEmail(), "login", user.getLogin(),
                "name", user.getName(), "birthday", user.getBirthday());
        long id = insert(sql, params);
        user.setId(id);

        return user;
    }

    public User update(User user) {
        String sql = "UPDATE users SET email = :email, login = :login, name = :name, birthday = :birthday WHERE id = :id";
        Map<String, Object> params = Map.of("email", user.getEmail(), "login", user.getLogin(), "name", user.getName(),
                "birthday", user.getBirthday(), "id", user.getId());
        update(sql, params);

        return user;
    }

    public Set<User> getFriends(User user) {
        String sql = "SELECT * FROM users WHERE id IN " +
                "(SELECT uf.friend_id FROM users u LEFT JOIN user_friend uf ON u.id = uf.user_id WHERE u.id = :id)";
        Map<String, Long> params = Map.of("id", user.getId());

        return new HashSet<>(findMany(sql, params));
    }

    public List<User> getCommonFriends(long userId, long otherId) {
        String sql = "WITH cte AS " +
                "(SELECT uf.friend_id " +
                " FROM users u " +
                " LEFT JOIN user_friend uf ON u.id = uf.user_id " +
                " WHERE u.id = :userId) " +
                "SELECT u1.* " +
                "FROM users u " +
                " LEFT JOIN user_friend uf ON u.id = uf.user_id " +
                " INNER JOIN cte ON uf.friend_id = cte.friend_id " +
                " INNER JOIN users u1 ON u1.id = uf.friend_id " +
                "WHERE u.id = :otherId";

        Map<String, Long> params = Map.of("userId", userId,
                "otherId", otherId);

        return findMany(sql, params);
    }

}