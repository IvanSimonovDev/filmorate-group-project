package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FkConstraintViolationException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.*;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {

    private final FilmRepository filmRepository;
    private final UserRepository userRepository;
    private final MpaRepository mpaRepository;
    private final GenreRepository genreRepository;
    private final DirectorRepository directorRepository;

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

        if (null != film.getDirectors()) {
            List<Long> ids = film.getDirectors().stream()
                    .map(Director::getId)
                    .toList();
            List<Director> directors = directorRepository.getByIds(ids);

            if (ids.size() != directors.size()) {
                throw new FkConstraintViolationException("Режиссер вне диапазона.");
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

    public List<Film> getSortedDirectorsFilms(long directorId, String sortBy) {

        directorRepository.getById(directorId)
                .orElseThrow(() -> new ValidationException("Режиссер c ID - " + directorId + ", не найден."));

        String order = SortOrder.from(sortBy);

        if ("null".equals(order)) {
            throw new ValidationException("Получено: " + sortBy + " Должно быть year или likes");
        }

        return filmRepository.getSortedDirectorsFilms(directorId, SortOrder.from(sortBy));

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

}
