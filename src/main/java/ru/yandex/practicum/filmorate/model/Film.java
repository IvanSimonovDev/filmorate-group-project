package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import ru.yandex.practicum.filmorate.validation.OnUpdate;
import ru.yandex.practicum.filmorate.validation.ReleaseDate;

import java.time.LocalDate;

/**
 * Film.
 */

@Data
public class Film {

    @NotNull(groups = OnUpdate.class)
    private Long id;

    @NotEmpty
    private String name;

    @Size(max = 200)
    private String description;

    @ReleaseDate
    private LocalDate releaseDate;

    @Positive()
    private Integer duration;

}
