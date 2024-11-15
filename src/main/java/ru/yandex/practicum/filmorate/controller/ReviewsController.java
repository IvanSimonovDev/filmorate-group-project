package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewsService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewsController {
    private final ReviewsService reviewsService;

    @PostMapping
    public Review createReview(@RequestBody Review review) {
        return reviewsService.createReview(review);
    }

    @PutMapping
    public Review updateReview(@RequestBody Review review) {
        return reviewsService.updateReview(review);
    }

    @DeleteMapping("/{id}")
    public void deleteReview(@PathVariable("id") long reviewId) {
        reviewsService.deleteReview(reviewId);
    }

    @GetMapping("/{id}")
    public Review getReview(@PathVariable("id") long reviewId) {
        return reviewsService.getReview(reviewId);
    }

    @GetMapping
    public List<Review> getReviews(@RequestParam Optional<Long> filmId, @RequestParam Optional<Long> count) {
        return reviewsService.getReviews(filmId, count);
    }

    @PutMapping("/{id}/like/{userId}")
    public Review setLike(@PathVariable("id") long reviewId, @PathVariable long userId) {
        return reviewsService.likeReview(reviewId, userId);
    }

    @PutMapping("/{id}/dislike/{userId}")
    public Review setDislike(@PathVariable("id") long reviewId, @PathVariable long userId) {
        return reviewsService.dislikeReview(reviewId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Review deleteLike(@PathVariable("id") long reviewId, @PathVariable long userId) {
        return reviewsService.deleteLikeOnReview(reviewId, userId);
    }

    @DeleteMapping("/{id}/dislike/{userId}")
    public Review deleteDislike(@PathVariable("id") long reviewId, @PathVariable long userId) {
        return reviewsService.deleteDislikeOnReview(reviewId, userId);
    }
}
