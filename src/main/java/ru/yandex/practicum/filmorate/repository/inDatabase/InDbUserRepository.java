package ru.yandex.practicum.filmorate.repository.inDatabase;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@Primary
public class InDbUserRepository extends InDbBaseRepository<User> implements UserRepository {

//    private static final String FIND_BY_ID_QUERY = "SELECT * FROM users WHERE id = ?";
//    private static final String FIND_ALL_QUERY = "SELECT * FROM users";
//    private static final String INSERT_QUERY = "INSERT INTO users (email, login, name, birthday)" +
//            "VALUES (?, ?, ?, ?)";
//    private static final String UPDATE_QUERY = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";
//    private static final String SELECT_FRIENDS_QUERY = "SELECT * FROM users WHERE id IN " +
//            "(SELECT uf.friend_id FROM users u LEFT JOIN user_friend uf ON u.id = uf.user_id WHERE u.id = ?)";
//    private static final String SELECT_COMMON_FRIENDS_QUERY = "WITH cte AS " +
//            "(SELECT uf.friend_id " +
//            " FROM users u " +
//            " LEFT JOIN user_friend uf ON u.id = uf.user_id " +
//            " WHERE u.id = ?) " +
//            "SELECT u1.* " +
//            "FROM users u " +
//            " LEFT JOIN user_friend uf ON u.id = uf.user_id " +
//            " INNER JOIN cte ON uf.friend_id = cte.friend_id " +
//            " INNER JOIN users u1 ON u1.id = uf.friend_id " +
//            "WHERE u.id = ?";


    public InDbUserRepository(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    public Optional<User> get(long userId) {
        String sql = "SELECT * FROM users WHERE id = ?";
        return findOne(sql, userId);
    }

    public List<User> getAll() {
        String sql = "SELECT * FROM users";

        return findMany(sql);
    }

    public User save(User user) {
        String sql = "INSERT INTO users (email, login, name, birthday)" +
                "VALUES (?, ?, ?, ?)";

        long id = insert(sql,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday()
        );
        user.setId(id);
        return user;
    }

    public User update(User user) {
        String sql = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";

                update(
                sql,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId()
        );
        return user;
    }

    public Set<User> getFriends(User user) {
        String sql = "SELECT * FROM users WHERE id IN " +
                "(SELECT uf.friend_id FROM users u LEFT JOIN user_friend uf ON u.id = uf.user_id WHERE u.id = ?)";

        return new HashSet<>(findMany(sql, user.getId()));
    }

    public List<User> getCommonFriends(long userId, long otherId) {
        String sql = "WITH cte AS " +
                "(SELECT uf.friend_id " +
                " FROM users u " +
                " LEFT JOIN user_friend uf ON u.id = uf.user_id " +
                " WHERE u.id = ?) " +
                "SELECT u1.* " +
                "FROM users u " +
                " LEFT JOIN user_friend uf ON u.id = uf.user_id " +
                " INNER JOIN cte ON uf.friend_id = cte.friend_id " +
                " INNER JOIN users u1 ON u1.id = uf.friend_id " +
                "WHERE u.id = ?";

        return findMany(sql, userId, otherId);
    }

}