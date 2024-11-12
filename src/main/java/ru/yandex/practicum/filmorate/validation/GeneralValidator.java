package ru.yandex.practicum.filmorate.validation;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;

@Component
public class GeneralValidator {
    public void validateContainerEntryPositiveOrNull(Integer containerToValidate, String entryParamName) {
        if (containerToValidate != null && containerToValidate <= 0) {
            throw new ValidationException("Параметр " + entryParamName + " должен быть положительным.");
        }
    }
}
