package ru.yandex.practicum.filmorate.repository.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.UserFriends;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserFriendsRowMapper implements RowMapper<UserFriends> {


    @Override
    public UserFriends mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserFriends uf = new UserFriends();
        uf.setUserId(rs.getLong("user_id"));
        uf.setFriendId(rs.getLong("friend_id"));
        uf.setConfirmed(rs.getBoolean("isConfirmed"));

        return uf;
    }
}
