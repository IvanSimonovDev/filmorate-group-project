package ru.yandex.practicum.filmorate.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class InMemoryFilmRepository implements FilmRepository {

    private final HashMap<Long, Film> films = new HashMap<>();
    private final HashMap<Long, Set<User>> filmsLikes = new HashMap<>();

    private Long filmId = 0L;


    private long generateFilmId() {
        return ++filmId;
    }

    public Optional<Film> get(Long filmId) {
        return Optional.ofNullable(films.get(filmId));
    }

    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }

    public Film save(Film film) {
        film.setId(generateFilmId());
        films.put(film.getId(), film);
        return film;
    }

    public Film update(Film film) {
        Film currentFilm = films.get(film.getId());
        if (currentFilm != null) {
            currentFilm.setName(film.getName());
            currentFilm.setDescription(film.getDescription());
            currentFilm.setReleaseDate(film.getReleaseDate());
            currentFilm.setDuration(film.getDuration());
            films.put(currentFilm.getId(), currentFilm);

            return currentFilm;
        }
        return null;
    }


    public void addLike(Film film, User user) {

        Set<User> fLikes = filmsLikes.computeIfAbsent(film.getId(), id -> new HashSet<>());
        fLikes.add(user);
    }

    public void deleteLike(Film film, User user) {

        Set<User> fLikes = filmsLikes.computeIfAbsent(film.getId(), id -> new HashSet<>());
        if (!fLikes.isEmpty()) {
            fLikes.remove(user);
        }
    }

    public List<Film> getPopular(long count) {

        Map<Long, Set<User>> sorted = filmsLikes.entrySet().stream()
                .sorted(Comparator.comparingInt((Map.Entry<Long, Set<User>> e) -> e.getValue().size()).reversed())
                .collect(
                        Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue,
                                (e1, e2) -> e1,
                                LinkedHashMap::new));

        return sorted.keySet().stream()
//                .peek(id -> System.out.println(films.get(id)))
                .map(films::get)
                .limit(count)
                .toList();
    }


}