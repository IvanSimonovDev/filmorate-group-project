package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;
import java.util.Optional;

public interface DirectorRepository {

    List<Director> getAll();

    Optional<Director> getById(long id);

    List<Director> getByIds(List<Long> ids);

    Director save(Director director);

    Director update(Director director);

    void delete(final long directorId);

}







