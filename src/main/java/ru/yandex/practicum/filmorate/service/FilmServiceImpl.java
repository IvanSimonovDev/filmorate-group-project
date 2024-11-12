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
import ru.yandex.practicum.filmorate.validation.GeneralValidator;
import ru.yandex.practicum.filmorate.validation.GenreValidator;
import ru.yandex.practicum.filmorate.validation.ReleaseDateValidator;

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
    private final DirectorRepository directorRepository;
    private final ReleaseDateValidator releaseDateValidator;
    private final GenreValidator genreValidator;
    private final GeneralValidator generalValidator;

    public Film save(Film film) {
        film.setMpa(mpaRepository.getById(film.getMpa().getId())
                .orElseThrow(() -> new FkConstraintViolationException("Рейтинг вне диапазона."))
        );

        fillUpGenres(film);
        fillUpDirectors(film);
        return filmRepository.save(film);
    }

    public Film update(final Film film) {
        long filmId = film.getId();
        filmRepository.get(filmId)
                .orElseThrow(() -> new ValidationException("Фильм c ID - " + filmId + ", не найден."));

        mpaRepository.getById(film.getMpa().getId())
                .orElseThrow(() -> new FkConstraintViolationException("Рейтинг вне диапазона."));

        fillUpGenres(film);
        fillUpDirectors(film);

        return filmRepository.update(film);
    }

    public Film getById(long filmId) {
        return filmRepository.get(filmId)
                .orElseThrow(() -> new ValidationException("Фильм c ID - " + filmId + ", не найден."));
    }

    public void delete(final long filmId) {

        filmRepository.get(filmId)
                .orElseThrow(() -> new ValidationException("Фильм c ID - " + filmId + ", не найден."));

        filmRepository.delete(filmId);
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

        if (order.equals("null")) {
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

    public List<Film> recommendations(Long userId) {
        userRepository.get(userId)
                .orElseThrow(() -> new ValidationException("Пользователь c id: " + userId + " не найден"));
        return filmRepository.recommendations(userId);
    }

    public List<Film> getPopular(long count) {
        return filmRepository.getPopular(count);
    }

    public List<Film> getPopularByGenreAndYear(Integer count,
                                               Long genreId,
                                               Integer year) {
        generalValidator.validateContainerEntryPositiveOrNull(count, "count");
        genreValidator.validateGenreIdInContainer(genreId);
        releaseDateValidator.validateYearInContainer(year);

        return filmRepository.getPopularByGenreAndYear(count, genreId, year);
    }


    @Override
    public Collection<Film> getCommonFilms(final Long userId1, final Long userId2) {
        userRepository.get(userId1)
                .orElseThrow(() -> new ValidationException(format("Пользователь c id: %d не найден", userId1)));

        userRepository.get(userId2)
                .orElseThrow(() -> new ValidationException(format("Пользователь c id: %d не найден", userId2)));

        return filmRepository.getCommonFilms(userId1, userId2);
    }

    private void fillUpGenres(Film film) {
        if (null != film.getGenres()) {
            List<Long> ids = film.getGenres().stream()
                    .map(Genre::getId)
                    .toList();
            List<Genre> genres = genreRepository.getByIds(ids);

            if (ids.size() != genres.size()) {
                throw new FkConstraintViolationException("Жанр вне диапазона.");
            }
        }
    }

    private void fillUpDirectors(Film film) {
        if (null != film.getDirectors()) {
            List<Long> ids = film.getDirectors().stream()
                    .map(Director::getId)
                    .toList();
            List<Director> directors = directorRepository.getByIds(ids);

            if (ids.size() != directors.size()) {
                throw new FkConstraintViolationException("Режиссер вне диапазона.");
            }
        }
    }
}
