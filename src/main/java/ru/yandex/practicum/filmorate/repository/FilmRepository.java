package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmRepository {


    Optional<Film> getFilm(Long filmId);

    List<Film> getAllFilms();

    void save(Film film);

    Film update(Film film);


}
