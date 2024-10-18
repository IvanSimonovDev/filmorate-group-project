package ru.yandex.practicum.filmorate.exception;

public class FKConstraintViolationException extends RuntimeException {
    public FKConstraintViolationException(String message) {
        super(message);
    }
}

