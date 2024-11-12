package ru.yandex.practicum.filmorate.validation;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;

@Service
public class GeneralValidator {
    public void validateContainerEntryPositiveOrNull(Integer containerToValidate, String entryParamName) {
        if (containerToValidate != null && containerToValidate <= 0) {
            throw new ValidationException("Параметр " + entryParamName + " должен быть положительным.");
        }
    }
}
