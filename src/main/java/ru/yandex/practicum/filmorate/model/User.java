package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.validation.OnUpdate;

import java.time.LocalDate;

@Data
@Slf4j
public class User {

    @NotNull(groups = OnUpdate.class)
    private Long id;

    //    @Pattern(regexp = "^(.+)@(\\S+)$")
    @Email(regexp = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "некорретный email адрес")
    private String email;

    @NotEmpty
    private String login;

    private String name;

    @Past
    private LocalDate birthday;

}
