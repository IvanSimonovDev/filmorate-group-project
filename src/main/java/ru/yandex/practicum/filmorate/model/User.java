package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.validation.OnUpdate;

import java.time.LocalDate;

@Data
@Slf4j
public class User {

    @NotNull(groups = OnUpdate.class)
    private Long id;

    @Email(regexp = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "некорретный email адрес")
    private String email;

    @Pattern(regexp = "^\\S+$", message = "login не может быть пустым и содержать пробелы")
    private String login;

    private String name;

    @Past
    private LocalDate birthday;

}
