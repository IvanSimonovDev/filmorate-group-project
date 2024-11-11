package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.UserRepository;
import ru.yandex.practicum.filmorate.repository.inDatabase.JdbcFilmRepository;
import ru.yandex.practicum.filmorate.repository.inDatabase.JdbcUserFriendRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JdbcUserFriendRepository userFriendRepository;
    private final JdbcFilmRepository filmRepository;


    public List<User> getAll() {
        return userRepository.getAll();
    }

    public User getById(long id) {

        return userRepository.get(id).orElseThrow(() -> new ValidationException("Пользователь c ID - " + id + ", не найден."));
    }

    public User save(final User user) {

        checkUserName(user);
        return userRepository.save(user);
    }

    public User update(final User user) {
        long userId = user.getId();
        userRepository.get(userId)
                .orElseThrow(() -> new ValidationException("Пользователь c ID - " + user.getId() + ", не найден."));

        checkUserName(user);
        return userRepository.update(user);
    }

    public void delete(final long userId) {
        userRepository.get(userId)
                .orElseThrow(() -> new ValidationException("Пользователь c ID - " + userId + ", не найден."));

        userRepository.delete(userId);
    }

    public void addFriend(long userId, long friendId, boolean isConfirmed) {
        final User user = userRepository.get(userId)
                .orElseThrow(() -> new ValidationException("Пользователь c id: " + userId + " не найден"));

        final User friend = userRepository.get(friendId)
                .orElseThrow(() -> new ValidationException("Пользователь c id: " + friendId + " не найден"));

        userFriendRepository.add(user, friend, isConfirmed);
    }

    public void deleteFriend(long userId, long friendId) {
        User user = userRepository.get(userId)
                .orElseThrow(() -> new ValidationException("Пользователь c id: " + userId + " не найден"));

        User friend = userRepository.get(friendId)
                .orElseThrow(() -> new ValidationException("Пользователь c id: " + friendId + " не найден"));

        userFriendRepository.delete(user, friend);
    }

    public Set<User> getFriends(long userId) {

        final User user = userRepository.get(userId)
                .orElseThrow(() -> new ValidationException("Пользователь c id: " + userId + " не найден"));

        return userRepository.getFriends(user);
    }

    public List<User> getCommonFriends(long userId, long otherId) {

        User user = userRepository.get(userId)
                .orElseThrow(() -> new ValidationException("Пользователь c id: " + userId + " не найден"));

        User other = userRepository.get(otherId)
                .orElseThrow(() -> new ValidationException("Пользователь c id: " + otherId + " не найден"));

        return userRepository.getCommonFriends(user.getId(), other.getId());
    }

    private void checkUserName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

}
