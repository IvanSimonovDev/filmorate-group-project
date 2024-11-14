package ru.yandex.practicum.filmorate.repository.inDatabase;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.repository.ReviewsLikesDislikesRepository;

import java.util.Map;

@Repository
public class JdbcReviewsLikesDislikesRepository extends JdbcBaseRepository<Review> implements ReviewsLikesDislikesRepository {

    private static final String insertSql = """
            INSERT INTO reviews_likes_dislikes (reviewId, userId, val)
            VALUES (:reviewId, :userId, :val);
            """;

    private static final String deleteSql = """
            DELETE FROM reviews_likes_dislikes
            WHERE (reviewId = :reviewId)
            AND (userID = :userId)
            AND (val = :val);
            """;

    public JdbcReviewsLikesDislikesRepository(NamedParameterJdbcOperations jdbc) {
        super(jdbc, null);
    }

    public void likeReview(long reviewId, long userId) {
        insert(insertSql, createParamsMap(reviewId, userId, 1));
    }

    public void dislikeReview(long reviewId, long userId) {
        insert(insertSql, createParamsMap(reviewId, userId, -1));
    }

    public void deleteLikeOnReview(long reviewId, long userId) {
        delete(deleteSql, createParamsMap(reviewId, userId, 1));
    }

    public void deleteDislikeOnReview(long reviewId, long userId) {
        delete(deleteSql, createParamsMap(reviewId, userId, -1));
    }

    public long getReviewRating(long reviewId) {
        String getCountSql = """
                SELECT COUNT(val)
                FROM reviews_likes_dislikes
                WHERE reviewId = :reviewId;
                """;
        String getRatingSql = """
                SELECT SUM(val)
                FROM reviews_likes_dislikes
                WHERE reviewId = :reviewId
                GROUP BY reviewId;
                """;

        Map<String, Long> params = Map.of("reviewId", reviewId);

        Long count = jdbc.queryForObject(getCountSql, params, Long.class);

        return (count == 0 ? 0 : jdbc.queryForObject(getRatingSql, params, Long.class));
    }

    private Map<String, Long> createParamsMap(long reviewId, long userId, long val) {
        return Map.of("reviewId", reviewId,
                "userId", userId,
                "val", val);
    }
}
