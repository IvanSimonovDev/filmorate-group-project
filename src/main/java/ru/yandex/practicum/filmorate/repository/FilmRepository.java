package ru.yandex.practicum.filmorate.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
@Slf4j
public class FilmRepository {

    private final HashMap<Long, Film> films = new HashMap<>();
    Long filmId = 0L;


    private long generateFilmId () {
        return ++filmId;
    }

    public Film getFilm(Long filmId) {

        if (films.get(filmId)!=null) {
            return films.get(filmId);
        } else {
            log.warn("Фильм c id {} не найден", filmId);
            throw new ValidationException("Фильм не найден");
        }
    }

    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    public void save(Film film) {
        film.setId(generateFilmId());
        films.put(film.getId(),film);
    }

    public Film update(Film film) {
        try {
            Film currentFilm = films.get(film.getId());
            currentFilm.setName(film.getName());
            currentFilm.setDescription(film.getDescription());
            currentFilm.setReleaseDate(film.getReleaseDate());
            currentFilm.setDuration(film.getDuration());
            films.put(currentFilm.getId(), currentFilm);

            return currentFilm;

    } catch (NullPointerException e) {
        log.warn("Фильм c id {} не найден", film.getId());
        throw new ValidationException("Фильм не найден");
    }
    }




}
