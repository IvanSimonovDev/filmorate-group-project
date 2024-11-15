package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;
import ru.yandex.practicum.filmorate.validation.OnUpdate;

import java.util.List;

@RestController
@RequestMapping("/directors")
@Slf4j
@Validated
@RequiredArgsConstructor
public class DirectorController {

    private final DirectorService service;

    @GetMapping
    public List<Director> getAll() {
        log.info("GET /Directors <--> Get all genres");
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Director getById(@PathVariable("id") @Positive long id) {
        log.info("GET /Directors/id <--> Get Director by ID");
        return service.getById(id);
    }

    @PostMapping
    public Director save(@Valid @RequestBody Director director) {
        log.info("POST /directors --> Create Director: {} - started", director);
        Director savedDirector = service.save(director);
        log.info("POST /directors <-- Create Director: {} - ended", director);
        return savedDirector;
    }

    @PutMapping
    public Director update(@Valid @Validated(OnUpdate.class) @RequestBody Director director) {
        log.info("PUT /directors --> Update Director: {} - started", director);
        Director updatedDirector = service.update(director);
        log.info("PUT /directors <-- Update Director: {} - ended", director);
        return updatedDirector;
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") long id) {
        log.info("DELETE /directors/id --> deleting Director {} - started", id);
        service.delete(id);
        log.info("DELETE /directors/id <-- deleting Director {} - ended", id);
    }

}
