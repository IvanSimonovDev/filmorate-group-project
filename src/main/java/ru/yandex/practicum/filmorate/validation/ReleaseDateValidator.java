package ru.yandex.practicum.filmorate.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.time.LocalDate;

@Component
public class ReleaseDateValidator implements ConstraintValidator<ReleaseDate, LocalDate> {

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (value != null) {
            return value.isAfter(LocalDate.of(1895, 12, 28));
        }
        return false;
    }

    public void validateYearInContainer(Integer yearContainer) {
        if (yearContainer != null && yearContainer < 1895) {
            throw new ValidationException("Год должен быть не меньше 1895.");
        }
    }
}
