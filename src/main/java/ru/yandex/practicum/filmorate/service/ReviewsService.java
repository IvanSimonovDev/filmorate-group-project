package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;
import java.util.Optional;

public abstract class ReviewsService {

    public abstract Review createReview(Review review);

    public abstract Review updateReview(Review review);

    public abstract void deleteReview(long reviewId);

    public abstract Review getReview(long reviewId);

    public abstract List<Review> getReviews(Optional<Long> filmId, Optional<Long> count);

    public abstract Review likeReview(long reviewId, long userId);

    public abstract Review dislikeReview(long reviewId, long userId);

    public abstract Review deleteLikeOnReview(long reviewId, long userId);

    public abstract Review deleteDislikeOnReview(long reviewId, long userId);


}
