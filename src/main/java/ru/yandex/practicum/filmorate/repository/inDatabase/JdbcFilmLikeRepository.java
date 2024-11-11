package ru.yandex.practicum.filmorate.repository.inDatabase;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.FilmLike;
import ru.yandex.practicum.filmorate.repository.FilmLikeRepository;

import java.util.List;
import java.util.Map;

@Repository
public class JdbcFilmLikeRepository extends JdbcBaseRepository<FilmLike> implements FilmLikeRepository {

    public JdbcFilmLikeRepository(NamedParameterJdbcOperations jdbc, RowMapper<FilmLike> mapper) {
        super(jdbc, mapper);
    }


    public List<FilmLike> getRecommendations(Long userId) {
        String sql = """
              SELECT FL3.FILM_ID
                                   FROM FILM_LIKES FL3
                                   WHERE FL3.FILM_LIKES_USER_ID = (SELECT FL2.FILM_LIKES_USER_ID
                                                        FROM FILM_LIKES FL2
                                                        WHERE FL2.FILM_LIKES_USER_ID <> :film_likes_user_id
                                                          AND FL2.FILM_ID IN (SELECT FL1.FILM_ID
                                                                              FROM FILM_LIKES FL1
                                                                              WHERE FL1.FILM_LIKES_USER_ID = :film_likes_user_id
                                                            )
                                                        GROUP BY FL2.FILM_LIKES_USER_ID
                                                        ORDER BY COUNT(FL2.FILM_ID) DESC
                                                        LIMIT 1
                                                        )
            """;

        Map<String, Long> params = Map.of("film_likes_user_id", userId);
        return findMany(sql, params);
    }

    public List<FilmLike> getFilmsUserById(Long userId) {
        String sql = "SELECT film_id "
                + "FROM film_likes "
                + "WHERE film_likes_user_id = :film_likes_user_id";
        Map<String, Long> param = Map.of("film_likes_user_id", userId);
        return findMany(sql, param);
    }

    public List<FilmLike> getUserByFilmId(Long filmId) {
        String sql = "SELECT film_likes_user_id "
                + "FROM film_likes "
                + "WHERE film_id = :film_id";
        Map<String, Long> param = Map.of("film_id", filmId);
        return findMany(sql, param);
    }
}
