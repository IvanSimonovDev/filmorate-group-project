package ru.yandex.practicum.filmorate.repository.inDatabase;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.inDatabase.mapper.FilmExtractor;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@Primary
public class JdbcFilmRepository extends JdbcBaseRepository<Film> implements FilmRepository {

    private final JdbcFilmGenreRepository filmGenreRepository;
    private final JdbcGenreRepository genreRepository;
    private final FilmExtractor filmExtractor;
    private final FilmGenreRowMapper filmGenreRowMapper;

    protected record FilmGenre(long filmId, long genreId) {
    }

    public JdbcFilmRepository(NamedParameterJdbcOperations jdbc, RowMapper<Film> mapper, JdbcFilmGenreRepository filmGenreRepository,
                              JdbcGenreRepository genreRepository, FilmExtractor filmExtractor, FilmGenreRowMapper filmGenreRowMapper) {
        super(jdbc, mapper);
        this.filmGenreRepository = filmGenreRepository;
        this.genreRepository = genreRepository;
        this.filmExtractor = filmExtractor;
        this.filmGenreRowMapper = filmGenreRowMapper;
    }

    public Optional<Film> get(long filmId) {
        String sql = "SELECT * FROM film f JOIN mpa mpa ON f.RATING_ID = mpa.id " +
                "LEFT JOIN FILM_GENRE fg ON f.ID = fg.FILM_ID " +
                "LEFT JOIN GENRE g ON g.ID = fg.GENRE_ID " +
                " WHERE f.id = :filmId";

        Map<String, Object> params = Map.of("filmId", filmId);


        return Optional.ofNullable(jdbc.query(sql, params, filmExtractor));
    }

    public List<Film> getAll() {
        String sql = "SELECT * FROM film f JOIN mpa mpa ON f.RATING_ID = mpa.id";
        String sql2 = "SELECT * FROM film_genre";

        List<Genre> genres = genreRepository.getAll();
        List<Film> films = findMany(sql, Collections.emptyMap());
        List<FilmGenre> filmGenres = jdbc.query(sql2, filmGenreRowMapper);

        films.forEach(film -> {
            Set<Genre> associatedGenres = filmGenres.stream()
                    .filter(fg -> fg.filmId() == film.getId())
                    .flatMap(fg -> genres.stream()
                            .filter(genre -> genre.getId() == fg.genreId()))
                    .collect(Collectors.toSet());

            film.setGenres(associatedGenres);
        });
        return films;
    }

    public Film save(Film film) {
        String sql = "INSERT INTO film (name, description, release_date, duration, rating_id) " +
                "VALUES (:name, :description, :release_date, :duration, :rating_id)";

        Map<String, Object> params = Map.of("name", film.getName(), "description", film.getDescription(), "release_date", film.getReleaseDate(),
                "duration", film.getDuration(), "rating_id", film.getMpa().getId());

        long id = insert(sql, params);
        film.setId(id);

        if (null != film.getGenres()) {
            filmGenreRepository.save(film);
        }
        return film;
    }

    public Film update(Film film) {
        String sql = "UPDATE film SET name = :name, description = :description, release_date = :release_date, duration = :duration, " +
                "rating_id = :rating_id WHERE id = :id";
        Map<String, Object> params = Map.of("name", film.getName(), "description", film.getDescription(), "release_date", film.getReleaseDate(),
                "duration", film.getDuration(), "rating_id", film.getMpa().getId(), "id", film.getId());

        update(
                sql, params);
        if (null != film.getGenres()) {
            filmGenreRepository.delete(film);
            filmGenreRepository.save(film);
        }
        return film;
    }

    public void delete(final long filmId) {
        String sql = "DELETE from film WHERE id = :filmId";
        Map<String, Object> params = Map.of("filmId", filmId);

        delete(sql, params);
    }

    public void addLike(Film film, User user) {
        String sql = "INSERT INTO film_likes (film_id, user_id) VALUES (:film_id, :user_id)";
        Map<String, Object> params = Map.of("film_id", film.getId(), "user_id", user.getId());

        insert(sql, params);
    }

    public boolean deleteLike(Film film, User user) {
        String sql = "DELETE from film_likes WHERE  film_id = :film_id AND user_Id = :user_Id";
        Map<String, Long> params = Map.of("film_id", film.getId(), "user_Id", user.getId());

        return delete(sql, params);
    }

    public List<Film> getPopular(long count) {
        String sql = "SELECT f.*, mpa.ID, MPA.NAME, COUNT(fl.film_id) AS likes " +
                "FROM film f JOIN mpa mpa ON f.RATING_ID = mpa.id " +
                "LEFT JOIN film_likes fl ON f.id = fl.film_id " +
                "GROUP BY f.id, f.name, f.description, f.release_date, f.duration, mpa.ID, mpa.NAME " +
                "ORDER BY likes DESC LIMIT :count";
        Map<String, Long> params = Map.of("count", count);

        return findMany(sql, params);
    }

}
