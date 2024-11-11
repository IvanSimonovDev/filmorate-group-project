package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class FilmLike {
    private Long filmId = 0L;
    private Long userId = 0L;
}