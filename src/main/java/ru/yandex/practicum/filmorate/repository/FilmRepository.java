package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface FilmRepository {
    Optional<Film> get(long filmId);

    List<Film> getAll();

    Film save(Film film);

    Film update(Film film);

    void delete(long filmId);

    List<Film> getSortedDirectorsFilms(long directorId, String sortBy);

    List<Film> recommendations(Long userId);

    void addLike(Film film, User user);

    boolean deleteLike(Film film, User user);

    List<Film> getPopular(long count);

    List<Film> getPopularByGenreAndYear(Integer count, Long genreId, Integer year);

    Collection<Film> searchFilmsByParams(String query, String by);

    List<Film> getCommonFilms(Long userId1, Long userId2);

    List<Film> fillUpDirectors(List<Film> films);

    void fillUpGenres(List<Film> films);

    Film fillUp(Film film);
}
