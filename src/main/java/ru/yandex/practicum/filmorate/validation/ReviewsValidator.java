package ru.yandex.practicum.filmorate.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.ReviewsRepository;
import ru.yandex.practicum.filmorate.repository.UserRepository;

@RequiredArgsConstructor
@Service
public class ReviewsValidator {
    private final UserRepository userRepository;
    private final FilmRepository filmRepository;
    private final ReviewsRepository reviewsRepository;

    public void validateNew(Review review) {
        validateContentUserIdFilmId(review);
    }

    public void validateUpdated(Review review) {
        validateReviewId(review.getReviewId());
        validateContentUserIdFilmId(review);
    }

    public void validateReviewId(long reviewID) {
        reviewsRepository.get(reviewID)
                .orElseThrow(() -> new ValidationException("Отзыв с id: " + reviewID + " не найден"));
    }

    public void validateContent(String content) {
        if (content.isBlank()) {
            throw new ValidationException("Строка пустая или состоит лишь из пробелов");
        }
    }

    public void validateUserId(Long userId) {
        if (userId == null) {
            throw new FieldNotSetException("Пользователь не указан.");
        }
        userRepository.get(userId)
                .orElseThrow(() -> new ValidationException("Пользователь c id: " + userId + " не найден"));
    }

    public void validateFilmId(Long filmId) {
        if (filmId == null) {
            throw new FieldNotSetException("Фильм не указан.");
        }
        filmRepository.get(filmId)
                .orElseThrow(() -> new ValidationException("Фильм c id: " + filmId + " не найден"));
    }

    public void validateCount(long count) {
        if (count < 0) {
            throw new ValidationException("Count < 0");
        }
    }

    public void validateContentUserIdFilmId(Review review) {
        validateContent(review.getContent());
        validateFilmId(review.getFilmId());
        validateUserId(review.getUserId());
    }
}
