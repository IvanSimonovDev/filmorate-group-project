package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepository {


    Optional<User> getUser(long userId);

    List<User> getAllUsers();

    User save(User user);

    User update(User user);

    void addFriend(User user, User friend);
    void deleteFriend(User user, User friend);

    Set<User> getFriends(User user);

    List<User> getCommonFriends(long userId, long otherId);


}
