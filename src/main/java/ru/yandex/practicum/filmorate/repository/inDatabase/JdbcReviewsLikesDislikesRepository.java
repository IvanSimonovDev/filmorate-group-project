package ru.yandex.practicum.filmorate.repository.inDatabase;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.repository.ReviewsLikesDislikesRepository;

@Repository
@RequiredArgsConstructor
public class JdbcReviewsLikesDislikesRepository implements ReviewsLikesDislikesRepository {
    private final JdbcTemplate jdbc;
    private static final String insertSql = """
            INSERT INTO reviews_likes_dislikes (reviewId, userId, val)
            VALUES (?, ?, ?);
            """;

    private static final String deleteSql = """
            DELETE FROM reviews_likes_dislikes
            WHERE (reviewId = ?)
            AND (userID = ?)
            AND (val = ?);
            """;

    public void likeReview(long reviewId, long userId) {
        jdbc.update(insertSql, reviewId, userId, 1);
    }

    public void dislikeReview(long reviewId, long userId) {
        jdbc.update(insertSql, reviewId, userId, -1);
    }

    public void deleteLikeOnReview(long reviewId, long userId) {
        jdbc.update(deleteSql, reviewId, userId, 1);
    }

    public void deleteDislikeOnReview(long reviewId, long userId) {
        jdbc.update(deleteSql, reviewId, userId, -1);
    }

    public long getReviewRating(long reviewId) {
        String getCountSql = """
                SELECT COUNT(val)
                FROM reviews_likes_dislikes
                WHERE reviewId = ?;
                """;
        String getRatingSql = """
                SELECT SUM(val)
                FROM reviews_likes_dislikes
                WHERE reviewId = ?
                GROUP BY reviewId;
                """;

        Long count = jdbc.queryForObject(getCountSql, Long.class, reviewId);

        return (count == 0 ? 0 : jdbc.queryForObject(getRatingSql, Long.class, reviewId));
    }
}
