package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.repository.ReviewsLikesDislikesRepository;
import ru.yandex.practicum.filmorate.repository.ReviewsRepository;
import ru.yandex.practicum.filmorate.validation.ReviewsValidator;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ReviewsService {
    private final ReviewsValidator reviewsValidator;
    private final ReviewsRepository reviewsRepository;
    private final ReviewsLikesDislikesRepository reviewsLikesDislikesRepository;

    public Review createReview(Review review) {
        reviewsValidator.validateNew(review);
        return reviewsRepository.create(review);
    }

    public Review updateReview(Review review) {
        reviewsValidator.validateUpdated(review);
        return setReviewRatingAndReturn(reviewsRepository.update(review));
    }

    public void deleteReview(long reviewId) {
        //reviewsValidator.validateReviewId(reviewId);
        reviewsRepository.delete(reviewId);
    }

    public Review getReview(long reviewId) {
        reviewsValidator.validateReviewId(reviewId);
        return setReviewRatingAndReturn(reviewsRepository.get(reviewId).get());
    }

    public List<Review> getReviews(Optional<Long> filmId, Optional<Long> count) {
        filmId.ifPresent(id -> reviewsValidator.validateFilmId(id));
        long correctCountValue = 5;
        reviewsValidator.validateCount(count.orElse(correctCountValue));
        List<Review> listOfReviews = reviewsRepository.getSome(filmId, count);
        listOfReviews = listOfReviews.stream().map(this::setReviewRatingAndReturn).toList();
        return listOfReviews;
    }

    public Review likeReview(long reviewId, long userId) {
        reviewsValidator.validateReviewId(reviewId);
        reviewsValidator.validateUserId(userId);
        reviewsLikesDislikesRepository.deleteDislikeOnReview(reviewId, userId);
        reviewsLikesDislikesRepository.deleteLikeOnReview(reviewId, userId);
        reviewsLikesDislikesRepository.likeReview(reviewId, userId);
        return getReview(reviewId);
    }

    public Review dislikeReview(long reviewId, long userId) {
        reviewsValidator.validateReviewId(reviewId);
        reviewsValidator.validateUserId(userId);
        reviewsLikesDislikesRepository.deleteLikeOnReview(reviewId, userId);
        reviewsLikesDislikesRepository.deleteDislikeOnReview(reviewId, userId);
        reviewsLikesDislikesRepository.dislikeReview(reviewId, userId);
        return getReview(reviewId);
    }

    public Review deleteLikeOnReview(long reviewId, long userId) {
        reviewsValidator.validateReviewId(reviewId);
        reviewsValidator.validateUserId(userId);
        reviewsLikesDislikesRepository.deleteLikeOnReview(reviewId, userId);
        return getReview(reviewId);
    }

    public Review deleteDislikeOnReview(long reviewId, long userId) {
        reviewsValidator.validateReviewId(reviewId);
        reviewsValidator.validateUserId(userId);
        reviewsLikesDislikesRepository.deleteDislikeOnReview(reviewId, userId);
        return getReview(reviewId);
    }


    private Review setReviewRatingAndReturn(Review review) {
        review.setUseful(reviewsLikesDislikesRepository.getReviewRating(review.getReviewId()));
        return review;
    }


}
