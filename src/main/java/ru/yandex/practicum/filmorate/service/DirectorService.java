package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

public interface DirectorService {

    List<Director> getAll();

    Director getById(long id);

    Director save(Director director);

    Director update(Director director);

    void delete(final long directorId);

}
