package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreRepository {
    List<Genre> getAll();

    Optional<Genre> getById(long id);

    List<Genre> getByIds(List<Long> ids);

    List<Genre> getAllGenresByFilmId(long filmId);
}
