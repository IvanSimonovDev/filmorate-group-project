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
            String exceptionMessageTemplate = "Жанр с id = %d не существует.";
            String exceptionMessage = String.format(exceptionMessageTemplate, genreIdContainer);
            ValidationException exception = new ValidationException(exceptionMessage);
            genreRepository.getById(genreIdContainer).orElseThrow(() -> exception);
        }
    }
}
