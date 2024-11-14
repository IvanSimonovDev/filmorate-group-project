package ru.yandex.practicum.filmorate.repository;

public interface ReviewsLikesDislikesRepository {

    public void likeReview(long reviewId, long userId);

    public void dislikeReview(long reviewId, long userId);

    public void deleteLikeOnReview(long reviewId, long userId);

    public void deleteDislikeOnReview(long reviewId, long userId);

    public long getReviewRating(long reviewId);
}
