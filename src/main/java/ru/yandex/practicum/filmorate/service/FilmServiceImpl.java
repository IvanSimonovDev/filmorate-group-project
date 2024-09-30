package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {

    private final FilmRepository filmRepository;
    private final UserRepository userRepository;


    public Film save(Film film) {
        return filmRepository.save(film);
    }

    public Film update(final Film film) {

        long filmId = film.getId();
        filmRepository.get(filmId)
                .orElseThrow(() -> new ValidationException("Фильм c ID - " + filmId + ", не найден."));

        return filmRepository.update(film);
    }

    public Film getById(long id) {

        return filmRepository.get(id).orElseThrow(() -> new ValidationException("Фильм c ID - " + id + ", не найден."));
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
}
