package ru.yandex.practicum.filmorate.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.repository.GenreRepository;

@Service
@RequiredArgsConstructor
public class GenreValidator {
    private final GenreRepository genreRepository;

    public void validateGenreIdInContainer(Long genreIdContainer) {
        if (genreIdContainer != null) {
            String exceptionMessage = "Жанр с id = " + genreIdContainer + " не существует.";
            ValidationException exception = new ValidationException(exceptionMessage);
            genreRepository.getById(genreIdContainer).orElseThrow(() -> exception);
        }
    }
}
