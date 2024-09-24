package ru.yandex.practicum.filmorate.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Repository
@Slf4j
public class InMemoryUserRepository implements UserRepository {

    private final HashMap<Long, User> users = new HashMap<>();
    private final HashMap<Long, Set<User>> usersFriends = new HashMap<>();
    private Long userId = 0L;


    private long generateUserId() {
        return ++userId;
    }

    public Optional<User> getUser(long userId) {
        return Optional.ofNullable(users.get(userId));
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public User save(final User user) {
        user.setId(generateUserId());
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        return user;
    }

    public User update(User user) {
        User currentUser = users.get(user.getId());
        currentUser.setEmail(user.getEmail());
        currentUser.setLogin(user.getLogin());
        if (!user.getName().isBlank()) {
            currentUser.setName(user.getName());
        }
        currentUser.setBirthday(user.getBirthday());
        users.put(currentUser.getId(), currentUser);
        return currentUser;
    }


    public void addFriend(User user, User friend) {

        Set<User> uFriends = usersFriends.computeIfAbsent(user.getId(), id -> new HashSet<>());
        uFriends.add(friend);

        Set<User> fFriends = usersFriends.computeIfAbsent(friend.getId(), id -> new HashSet<>());
        fFriends.add(user);

    }


    public void deleteFriend(User user, User friend) {

        Set<User> uFriends = usersFriends.computeIfAbsent(user.getId(), id -> new HashSet<>());
        uFriends.remove(friend);

        Set<User> fFriends = usersFriends.computeIfAbsent(friend.getId(), id -> new HashSet<>());
        fFriends.remove(user);
    }


    public Set<User> getFriends(User user) {

        /*return getUser(user.getId()).orElseThrow().getFriends().stream()
                .map(friend -> getUser(friend).orElseThrow())
                .toList();*/
        return usersFriends.get(user.getId());
    }

    public List<User> getCommonFriends(long userId, long otherId) {

        List<User> commonFriends = usersFriends.get(userId).stream()
                .filter(user -> usersFriends.get(otherId).contains(user))
                .toList();
        return commonFriends;
    }

}
