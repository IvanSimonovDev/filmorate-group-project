package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Review {
    private long reviewId;
    private String content;
    private Boolean isPositive;
    private Long userId;
    private Long filmId;
    private long useful;
}
