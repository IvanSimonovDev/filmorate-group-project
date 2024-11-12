package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.validation.OnUpdate;
import ru.yandex.practicum.filmorate.validation.SearchParamBy;

import java.util.Collection;
import java.util.List;


@RestController()
@RequestMapping("/films")
@Slf4j
@Validated
@RequiredArgsConstructor
public class FilmController {

    private final FilmService service;

    @PostMapping
    public Film save(@Valid @RequestBody Film film) {
        log.info("POST /films --> Create Film: {} - started", film);
        service.save(film);
        log.info("POST /films <-- Create Film: {} - ended", film);

        return film;
    }

    @PutMapping
    public Film update(@Valid @Validated(OnUpdate.class) @RequestBody Film film) {
        log.info("PUT /films --> Update Film: {} - started", film);
        Film updatedFilm = service.update(film);
        log.info("PUT /films <-- Update Film: {} - ended", film);
        return updatedFilm;
    }

    @GetMapping
    public List<Film> getAll() {
        log.info("GET /films <--> Get all films");
        return service.getAll();
    }

    @GetMapping("{id}")
    public Film getById(@PathVariable("id") @Positive long id) {
        log.info("GET /films/id <--> Get film by id");
        return service.getById(id);
    }

    //    пользователь ставит лайк фильму.
    @PutMapping("{id}/like/{userId}")
    public void addLike(@PathVariable("id") long filmId, @PathVariable("userId") long userId) {
        log.info("PUT /films/filmId/like/userId --> User {} adding like for Film {} - started", userId, filmId);
        service.addLike(filmId, userId);
        log.info("PUT /films/filmId/like/userId <-- User {} adding like for Film {} - ended", userId, filmId);

    }

    //    пользователь удаляет лайк
    @DeleteMapping("{id}/like/{userId}")
    public void deleteLike(@PathVariable("id") long filmId, @PathVariable("userId") long userId) {
        log.info("DELETE /films/filmId/like/userId --> User {} deleting like for Film {} - started", userId, filmId);
        service.deleteLike(filmId, userId);
        log.info("DELETE /films/filmId/like/userId <-- User {} deleting like for Film {} - ended", userId, filmId);

    }

    //    возвращает список из первых count фильмов по количеству лайков. Если значение параметра count не задано, верните первые 10.
    @GetMapping("/popular")
    public List<Film> getPopular(@Positive @RequestParam(defaultValue = "10") long count) {

        log.info("GET /films/popular?count --> getting {} popular Films - started", count);
        List<Film> popularFilms = service.getPopular(count);
        log.info("GET /films/popular?count --> getting {} popular Films - ended", count);

        return popularFilms;
    }

    // GET /films/common?userId={userId}&friendId={friendId}
    @GetMapping("/common")
    public Collection<Film> getCommonFilms(@Positive @RequestParam Long userId, @Positive @RequestParam Long friendId) {
        log.info("GET /films/common?userId&friendId --> " +
                "getting movies between the user[id={}] and the user[id={}] - started", userId, friendId);

        Collection<Film> commonFilms = service.getCommonFilms(userId, friendId);

        log.info("GET /films/common?userId&friendId --> " +
                "getting movies between the user[id={}] and the user[id={}] - ended", userId, friendId);
        return commonFilms;
    }

    //    удаления фильма по идентификатору.
    //    DELETE /films/{filmId}
    @DeleteMapping("{filmId}")
    public void delete(@PathVariable("filmId") long filmId) {
        log.info("DELETE /films/filmId --> deleting Film {} - started", filmId);
        service.delete(filmId);
        log.info("DELETE /films/filmId <-- deleting Film {} - ended", filmId);
    }

    //    GET /films/director/{directorId}?sortBy=[year,likes]
    //    Возвращает список фильмов режиссера отсортированных по количеству лайков или году выпуска.
    @GetMapping("/director/{directorId}")
    public List<Film> getSortedDirectorsFilms(@NotEmpty @RequestParam String sortBy, @PathVariable("directorId") long directorId) {
        log.info("GET /films/director/{directorId}?sortBy --> getting Director {} Films sorted by {}  - started", directorId, sortBy);
        List<Film> directorsFilms = service.getSortedDirectorsFilms(directorId, sortBy);
        log.info("GET /films/director/{directorId}?sortBy <-- getting Director {} Films sorted by {}  - ended", directorId, sortBy);

        return directorsFilms;
    }

    @GetMapping("/search")
    public Collection<Film> search(@RequestParam String query, @RequestParam String by) {
        log.info("GET /films/search/?query&by --> getting Films by={}  query={}  - started", by, query);



        Collection<Film> foundFilms = service.search(query, by);
        log.info("GET /films/search/?query&by --> getting Films by={}  query={}  - ended", by, query);
        return foundFilms;
    }
}