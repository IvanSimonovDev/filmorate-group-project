package ru.yandex.practicum.filmorate.repository.inDatabase;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.repository.ReviewsRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class JdbcReviewsRepository extends JdbcBaseRepository<Review> implements ReviewsRepository {

    public JdbcReviewsRepository(NamedParameterJdbcOperations jdbc,
                                 RowMapper<Review> mapper) {
        super(jdbc, mapper);
    }

    public Review create(Review review) {

        String sql = """
                INSERT INTO reviews (content, isPositive, userId, filmId)
                VALUES (:content, :isPositive, :userId, :filmId);
                """;

        Map<String, Object> params = Map.of("content", review.getContent(),
                "isPositive", review.getIsPositive(),
                "userId", review.getUserId(),
                "filmId", review.getFilmId());

        long reviewId = insert(sql, params);

        return get(reviewId).get();
    }

    public Optional<Review> get(long reviewId) {
        String sql = """
                SELECT reviewId,
                       content,
                       isPositive,
                       userId,
                       filmId
                FROM reviews
                WHERE reviewId = :reviewId;
                """;

        Map<String, Object> params = Map.of("reviewId", reviewId);

        return findOne(sql, params);
    }

    public Review update(Review review) {
        String sql = """
                UPDATE reviews SET content = :content,
                                   isPositive = :isPositive
                WHERE reviewId = :reviewId;
                """;

        Map<String, Object> params = Map.of("content", review.getContent(),
                "isPositive", review.getIsPositive(),
                "reviewId", review.getReviewId());

        update(sql, params);
        return get(review.getReviewId()).get();
    }

    public void delete(long id) {
        String sql = """
                DELETE FROM reviews
                WHERE reviewId = :reviewId;
                """;

        Map<String, Object> params = Map.of("reviewId", id);
        delete(sql, params);
    }

    public List<Review> getSome(Optional<Long> filmId, Optional<Long> count) {
        StringBuilder sqlTemplate = new StringBuilder("""
                SELECT reviewId, content, isPositive, userId, filmId FROM reviews
                """);
        Map<String, Object> params = new HashMap<>();

        if (filmId.isPresent()) {
            sqlTemplate.append("""
                    \nWHERE filmId = :filmId
                    """);
            params.put("filmId", filmId.get());
        }
        if (count.isPresent()) {
            sqlTemplate.append("""
                    \nLIMIT :count
                    """);
            params.put("count", count.get());
        }
        return findMany(sqlTemplate.toString(), params);
    }
}
