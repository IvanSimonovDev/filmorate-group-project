package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepository {

    Optional<User> get(long userId);

    List<User> getAll();

    User save(User user);

    User update(User user);

    void delete(final long userId);

    Set<User> getFriends(User user);

    List<User> getCommonFriends(long userId, long otherId);

}
