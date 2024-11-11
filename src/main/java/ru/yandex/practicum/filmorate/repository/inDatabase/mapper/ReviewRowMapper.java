package ru.yandex.practicum.filmorate.repository.inDatabase.mapper;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Review;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ReviewRowMapper implements RowMapper<Review> {

    @Override
    public Review mapRow(ResultSet rs, int rowNum) throws SQLException, DataAccessException {
        Review review = new Review();
        review.setReviewId(rs.getLong("reviewId"));
        review.setContent(rs.getString("content"));
        review.setIsPositive(rs.getBoolean("isPositive"));
        review.setUserId(rs.getLong("userId"));
        review.setFilmId(rs.getLong("filmId"));
        review.setUseful(0);

        return review;
    }

}