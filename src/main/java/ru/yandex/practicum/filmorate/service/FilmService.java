package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.repository.FilmRepository;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmRepository filmRepository;




}
