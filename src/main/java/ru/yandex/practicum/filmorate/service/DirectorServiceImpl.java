package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.repository.DirectorRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class DirectorServiceImpl implements DirectorService {

    private final DirectorRepository repository;

    public List<Director> getAll() {
        return repository.getAll();
    }

    public Director getById(long id) {

        return repository.getById(id)
                .orElseThrow(() -> new ValidationException("Режиссер c ID - " + id + ", не найден."));
    }

    public Director save(final Director director) {

        return repository.save(director);
    }

    public Director update(final Director director) {

        long directorId = director.getId();
        repository.getById(directorId)
                .orElseThrow(() -> new ValidationException("Режиссер c ID - " + directorId + ", не найден."));

        return repository.update(director);
    }

    public void delete(final long id) {

        repository.getById(id)
                .orElseThrow(() -> new ValidationException("Режиссер c ID - " + id + ", не найден."));

        repository.delete(id);
    }



}
