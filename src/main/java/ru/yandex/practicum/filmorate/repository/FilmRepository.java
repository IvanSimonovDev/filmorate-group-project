package ru.yandex.practicum.filmorate.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class FilmRepository {

    private final HashMap<Long, Film> films = new HashMap<>();
    private Long filmId = 0L;


    private long generateFilmId() {
        return ++filmId;
    }

    public Optional<Film> getFilm(Long filmId) {
        return Optional.ofNullable(films.get(filmId));
    }

    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    public void save(Film film) {
        film.setId(generateFilmId());
        films.put(film.getId(), film);
    }

    public Film update(Film film) {
            Film currentFilm = films.get(film.getId());
            currentFilm.setName(film.getName());
            currentFilm.setDescription(film.getDescription());
            currentFilm.setReleaseDate(film.getReleaseDate());
            currentFilm.setDuration(film.getDuration());
            films.put(currentFilm.getId(), currentFilm);

            return currentFilm;
    }
}
