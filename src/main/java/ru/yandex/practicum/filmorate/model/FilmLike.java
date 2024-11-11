package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class FilmLike {
    private Long film_id = 0L;
    private Long user_id = 0L;
}