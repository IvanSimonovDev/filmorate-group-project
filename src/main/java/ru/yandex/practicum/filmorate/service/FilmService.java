package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

public interface FilmService {

    Film save(Film film);

    Film update(Film film);

    Film getById(long id);

    void delete(final long filmId);

    List<Film> getAll();

    List<Film> getSortedDirectorsFilms(long directorId, String sortBy);

    void addLike(long filmId, long userId);

    void deleteLike(long filmId, long userId);

    List<Film> getPopular(long count);

}
