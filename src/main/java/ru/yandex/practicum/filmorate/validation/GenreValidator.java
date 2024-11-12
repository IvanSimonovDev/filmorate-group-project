package ru.yandex.practicum.filmorate.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.repository.GenreRepository;

@Component
@RequiredArgsConstructor
public class GenreValidator {
    private final GenreRepository genreRepository;

    public void validateGenreIdInContainer(Long genreIdContainer) {
        if (genreIdContainer != null) {
            genreRepository.getById(genreIdContainer)
                    .orElseThrow(() -> new ValidationException(String.format("Жанр с id = %d не существует.", genreIdContainer)));
        }
    }
}
