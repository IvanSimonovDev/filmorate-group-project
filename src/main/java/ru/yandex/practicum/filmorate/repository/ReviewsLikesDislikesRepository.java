package ru.yandex.practicum.filmorate.repository;

public interface ReviewsLikesDislikesRepository {

    void likeReview(long reviewId, long userId);

    void dislikeReview(long reviewId, long userId);

    void deleteLikeOnReview(long reviewId, long userId);

    void deleteDislikeOnReview(long reviewId, long userId);

    long getReviewRating(long reviewId);
}
