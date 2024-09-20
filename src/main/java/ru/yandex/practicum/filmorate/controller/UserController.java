package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;
import ru.yandex.practicum.filmorate.validation.OnUpdate;

import java.util.List;

@RestController()
@RequestMapping("/users")
@Slf4j
@Validated // необходимо добавить @Validated в контроллер на уровне класса, чтобы проверять параметры метода.
            // В этом случае аннотация @Validated устанавливается на уровне класса, даже если она присутствует на методах.
@RequiredArgsConstructor
public class UserController {

    private final UserRepository repository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User saveUser(@Valid @RequestBody User user) {
        log.info("POST / Users --> Create User: {} - started", user);
        repository.save(user);
        log.info("POST / Users <-- Create User: {} - ended", user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @Validated(OnUpdate.class) @RequestBody User user) {
        try {
            log.info("PUT / Users --> Update User: {} - started", user);
            User updatedUser = repository.update(user);
            log.info("PUT / Users <-- Update User: {} - ended", user);
            return updatedUser;
        } catch (NullPointerException e) {
            log.warn("Пользователь c id {} не найден", user.getId());
            throw new ValidationException("Пользователь не найден");
        }

    }

    @GetMapping
    public List<User> getAllUsers() {
        return repository.getAllUsers();
    }

}

