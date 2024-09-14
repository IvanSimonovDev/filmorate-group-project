package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.validation.OnUpdate;

import java.util.List;
import java.util.Optional;


@RestController()
@RequestMapping("/films")
@Slf4j
@Validated
@RequiredArgsConstructor
public class FilmController {

    private final FilmRepository repository;

    @PostMapping
    public Film saveFilm(@Valid @RequestBody Film film) {
        log.info("POST /films --> Create Film: {} - started", film);
        repository.save(film);
        log.info("POST /films <-- Create Film: {} - ended", film);

        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @Validated(OnUpdate.class) @RequestBody Film film) {
        try {
            log.info("PUT /films --> Update Film: {} - started", film);
            Film updatedFilm = repository.update(film);
            log.info("PUT /films <-- Update Film: {} - ended", film);
            return updatedFilm;

        } catch (NullPointerException e) {
            log.warn("Фильм c id {} не найден", film.getId());
            throw new ValidationException("Фильм не найден");
        }
    }

    @GetMapping
    public List<Film> getAllFilms() {
        log.info("GET /films --> Get all films");
        return repository.getAllFilms();
    }

    @GetMapping("{id}")
    public Optional<Film> getFilmById(@PathVariable("id") @Positive Long id) {
        if (repository.getFilm(id).isPresent()) {
            log.info("GET /films/id --> Get film by id");
            return repository.getFilm(id);
        } else {
            log.warn("Фильм c id {} не найден", id);
            throw new ValidationException("Фильм не найден");
        }
    }

}