package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.inDatabase.InDbUserRepository;
import ru.yandex.practicum.filmorate.repository.inDatabase.mapper.UserRowMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import({InDbUserRepository.class, UserRowMapper.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserRepositoryTests {

    public static final long TEST_USER1_ID = 1L;
    public static final long TEST_USER2_ID = 2L;
    public static final long TEST_USER3_ID = 3L;
    public static final long TEST_NEWUSER_ID = 5L;
    private final InDbUserRepository userRepository;

    static User getTestUser() {
        User user = new User();
        user.setId(TEST_USER1_ID);
        user.setEmail("TestUser1@example.com");
        user.setLogin("TestUser1");
        user.setName("Bober Kurva");
        user.setBirthday(LocalDate.of(1990, 5, 15));
        return user;
    }

    static User getTestUser2() {
        User user = new User();
        user.setId(TEST_USER2_ID);
        user.setEmail("TestUser2@example.com");
        user.setLogin("TestUser2");
        user.setName("John Doe");
        user.setBirthday(LocalDate.of(1991, 1, 10));
        return user;
    }

    static User getTestUser3() {
        User user = new User();
        user.setId(TEST_USER3_ID);
        user.setEmail("TestUser3@example.com");
        user.setLogin("TestUser3");
        user.setName("Test User3");
        user.setBirthday(LocalDate.of(2000, 1, 10));
        return user;
    }

    static User getNewUser() {
        User user = new User();
        user.setId(TEST_NEWUSER_ID);
        user.setEmail("NewUser@example.com");
        user.setLogin("NewUser");
        user.setName("New User");
        user.setBirthday(LocalDate.of(1970, 11, 10));
        return user;
    }

    @Test
    public void shouldGetUserById() {
        User user = userRepository.get(TEST_USER1_ID).orElseThrow();
        assertThat(user)
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(getTestUser());
    }

    @DisplayName("Проверяем получение всех пользователей")
    @Test
    public void shouldGetAllUsers() {
        List<User> users = userRepository.getAll();
        assertThat(users)
                .isNotEmpty()
                .hasSize(4)
                .allMatch(Objects::nonNull)
                .contains(getTestUser(), getTestUser2());
    }

    @DisplayName("Добавляем нового пользователя")
    @Test
    public void shouldSaveUser() {
        userRepository.save(getNewUser());
        List<User> users = userRepository.getAll();

        assertThat(users)
                .isNotEmpty()
                .hasSize(5)
                .filteredOn(u -> Objects.equals(u.getId(), getNewUser().getId()))
                .first()
                .usingRecursiveComparison()
                .isEqualTo(getNewUser());
    }

    @DisplayName("Меняем данные существующего пользователя")
    @Test
    public void shouldUpdateUser() {
        getTestUser().setName("Bober Davis");
        getNewUser().setEmail("BoberNekurva@example.com");
        getTestUser().setId(5L);

        userRepository.update(getTestUser());
        Optional<User> optionalUser = userRepository.get(getTestUser().getId());

        assertThat(optionalUser)
                .isPresent()
                .hasValueSatisfying(user -> {
                    assertThat(user).hasFieldOrPropertyWithValue("name", getTestUser().getName());
                    assertThat(user).hasFieldOrPropertyWithValue("email", getTestUser().getEmail());
                });
    }

    @DisplayName("Проверяем получение списка друзей пользователя")
    @Test
    public void shouldGetFriends() {
        Set<User> friends = userRepository.getFriends(getTestUser());

        assertThat(friends)
                .isNotEmpty()
                .hasSize(2)
                .allMatch(Objects::nonNull)
                .contains(getTestUser2(), getTestUser3());
    }

    @DisplayName("Проверяем получение общих друзей пользователя")
    @Test
    public void shouldGetCommonFriends() {
        User commonFriends = userRepository.getCommonFriends(getTestUser().getId(), getTestUser2().getId()).getFirst();

        assertThat(commonFriends)
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(getTestUser3());
    }

}