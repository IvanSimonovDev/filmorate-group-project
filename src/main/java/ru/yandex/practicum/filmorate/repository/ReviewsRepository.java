package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewsRepository {
    Review create(Review review);

    Optional<Review> get(long id);

    Review update(Review review);

    void delete(long id);

    List<Review> getSome(Optional<Long> filmId, Optional<Long> count);
}