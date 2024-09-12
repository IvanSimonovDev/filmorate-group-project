package ru.yandex.practicum.filmorate.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
@Slf4j
public class UserRepository {

    private final HashMap<Long, User> users = new HashMap<>();
    Long userId = 0L;


    private long generateUserId() {
        return ++userId;
    }

    public User getUser(Long userId) {
        return users.get(userId);
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public void save(User user) {
        user.setId(generateUserId());
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
    }

    public User update(User user) {
        try {
            User currentUser = users.get(user.getId());
            currentUser.setEmail(user.getEmail());
            currentUser.setLogin(user.getLogin());
            if (!user.getName().isBlank()) {
                currentUser.setName(user.getName());
            }
            currentUser.setBirthday(user.getBirthday());
            return currentUser;

        } catch (NullPointerException e) {
            log.warn("Пользователь c id {} не найден", user.getId());
            throw new ValidationException("Пользователь не найден");
        }
    }
}
