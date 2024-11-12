package ru.yandex.practicum.filmorate.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.time.LocalDate;

@Service
public class ReleaseDateValidator implements ConstraintValidator<ReleaseDate, LocalDate> {

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (value != null) {
            return value.isAfter(LocalDate.of(1895, 12, 28));
        }
        return false;
    }

    public void validateYearInContainer(Integer yearContainer) {
        if (yearContainer != null) {
            LocalDate date = LocalDate.of(yearContainer, 12, 29);
            if (!isValid(date, null)) {
                throw new ValidationException("Год должен быть не меньше 1895.");
            }
        }
    }
}
