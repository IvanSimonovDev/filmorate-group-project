package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.MpaRepository;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class MpaService {

    private final MpaRepository mpaRepository;

    public List<Mpa> getAll() {
        return mpaRepository.getAll();
    }

    public Mpa getById(long id) {
        return mpaRepository.getById(id).orElseThrow(() -> new ValidationException("Rating c ID - " + id + ", не найден."));
    }

}
