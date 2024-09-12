package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.validation.OnUpdate;

import java.util.List;


@RestController()
@RequestMapping("/films")
@Slf4j
@Validated
public class FilmController {
    FilmRepository repository;

    public FilmController(final FilmRepository repository) {
        this.repository = repository;
    }


    @PostMapping
    public Film saveFilm(@Valid @RequestBody Film film) {
        log.info("POST /films --> Create Film: {} - started", film);
        repository.save(film);
        log.info("POST /films <-- Create Film: {} - ended", film);

        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @Validated(OnUpdate.class) @RequestBody Film film) {
        log.info("PUT /films --> Update Film: {} - started", film);
        Film updatedFilm = repository.update(film);
        log.info("PUT /films <-- Update Film: {} - ended", film);

        return updatedFilm;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        log.info("GET /films --> Get all films");
        return repository.getAllFilms();
    }

    @GetMapping("{id}")
    public Film getFilmById(@PathVariable("id") @Positive Long id) {
        log.info("GET /films/id --> Get film by id");
        return repository.getFilm(id);
    }



}