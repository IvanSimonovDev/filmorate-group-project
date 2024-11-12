package ru.yandex.practicum.filmorate.validation;

public class FieldNotSetException extends RuntimeException {
    public FieldNotSetException(String message) {
        super(message);
    }
}
