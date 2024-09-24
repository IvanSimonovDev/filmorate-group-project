package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class InMemoryUserService implements UserService {

    private final UserRepository userRepository;


    public List<User> getAll() {
        return userRepository.getAllUsers();
    }

    public User getById(long id) {

        return userRepository.getUser(id).orElseThrow(() -> new ValidationException("Пользователь c ID - " + id + ", не найден."));
    }

    public User save(final User user) {

        return userRepository.save(user);
    }


    public User update(final User user) {

        long userId = user.getId();
        final User existed = userRepository.getUser(userId)
                .orElseThrow(() -> new ValidationException("Пользователь c ID - " + user.getId() + ", не найден."));

        return userRepository.update(user);
    }

    public void addFriend(long userId, long friendId) {
        final User user = userRepository.getUser(userId)
                .orElseThrow(() -> new ValidationException("Пользователь c id: " + userId + " не найден"));

        final User friend = userRepository.getUser(friendId)
                .orElseThrow(() -> new ValidationException("Пользователь c id: " + friendId + " не найден"));

        userRepository.addFriend(user, friend);
    }

    public void deleteFriend(long userId, long friendId) {
        User user = userRepository.getUser(userId)
                .orElseThrow(() -> new ValidationException("Пользователь c id: " + userId + " не найден"));

        User friend = userRepository.getUser(friendId)
                .orElseThrow(() -> new ValidationException("Пользователь c id: " + friendId + " не найден"));

        userRepository.deleteFriend(user, friend);
    }

    public Set<User> getFriends(long userId) {

        final User user = userRepository.getUser(userId)
                .orElseThrow(() -> new ValidationException("Пользователь не найден"));

        return userRepository.getFriends(user) != null ? userRepository.getFriends(user) : Collections.emptySet();

    }

    public List<User> getCommonFriends(long userId, long otherId) {

        User user = userRepository.getUser(userId)
                .orElseThrow(() -> new ValidationException("Пользователь c id: " + userId + " не найден"));

        User friend = userRepository.getUser(otherId)
                .orElseThrow(() -> new ValidationException("Пользователь c id: " + otherId + " не найден"));

        return userRepository.getCommonFriends(userId, otherId);

    }


}
