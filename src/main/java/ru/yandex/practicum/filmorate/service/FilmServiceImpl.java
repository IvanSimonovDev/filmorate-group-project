package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FkConstraintViolationException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.GenreRepository;
import ru.yandex.practicum.filmorate.repository.MpaRepository;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.util.Collection;
import java.util.List;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {

    private final FilmRepository filmRepository;
    private final UserRepository userRepository;
    private final MpaRepository mpaRepository;
    private final GenreRepository genreRepository;

    public Film save(Film film) {
        film.setMpa(mpaRepository.getById(film.getMpa().getId())
                .orElseThrow(() -> new FkConstraintViolationException("Рейтинг вне диапазона."))
        );

        if (null != film.getGenres()) {
            List<Long> ids = film.getGenres().stream()
                    .map(Genre::getId)
                    .toList();
            List<Genre> genres = genreRepository.getByIds(ids);

            if (ids.size() != genres.size()) {
                throw new FkConstraintViolationException("Жанр вне диапазона.");
            }
        }
        return filmRepository.save(film);
    }

    public Film update(final Film film) {

        long filmId = film.getId();
        filmRepository.get(filmId)
                .orElseThrow(() -> new ValidationException("Фильм c ID - " + filmId + ", не найден."));

        mpaRepository.getById(film.getMpa().getId())
                .orElseThrow(() -> new FkConstraintViolationException("Рейтинг вне диапазона."));

        if (null != film.getGenres()) {
            List<Long> ids = film.getGenres().stream()
                    .map(Genre::getId)
                    .toList();
            List<Genre> genres = genreRepository.getByIds(ids);

            if (ids.size() != genres.size()) {
                throw new FkConstraintViolationException("Жанр вне диапазона.");
            }
        }

        return filmRepository.update(film);
    }

    public Film getById(long filmId) {

        return filmRepository.get(filmId)
                .orElseThrow(() -> new ValidationException("Фильм c ID - " + filmId + ", не найден."));
    }

    public List<Film> getAll() {
        return filmRepository.getAll();
    }

    public void addLike(long filmId, long userId) {

        final Film film = filmRepository.get(filmId)
                .orElseThrow(() -> new ValidationException("Фильм c ID - " + filmId + ", не найден."));

        final User user = userRepository.get(userId)
                .orElseThrow(() -> new ValidationException("Пользователь c id: " + userId + " не найден"));

        filmRepository.addLike(film, user);
    }

    public void deleteLike(long filmId, long userId) {

        final Film film = filmRepository.get(filmId)
                .orElseThrow(() -> new ValidationException("Фильм c ID - " + filmId + ", не найден."));

        final User user = userRepository.get(userId)
                .orElseThrow(() -> new ValidationException("Пользователь c id: " + userId + " не найден"));

        filmRepository.deleteLike(film, user);
    }

    public List<Film> getPopular(long count) {
        return filmRepository.getPopular(count);
    }

    @Override
    public Collection<Film> getCommonFilms(final Long userId1, final Long userId2) {
        userRepository.get(userId1)
                .orElseThrow(() -> new ValidationException(format("Пользователь c id: %d не найден", userId1)));

        userRepository.get(userId2)
                .orElseThrow(() -> new ValidationException(format("Пользователь c id: %d не найден", userId2)));

        return filmRepository.getCommonFilms(userId1, userId2);
    }

}
