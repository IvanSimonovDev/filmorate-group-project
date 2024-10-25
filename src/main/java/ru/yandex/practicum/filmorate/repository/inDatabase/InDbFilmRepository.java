package ru.yandex.practicum.filmorate.repository.inDatabase;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.inDatabase.mapper.FilmExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@Primary
public class InDbFilmRepository extends InDbBaseRepository<Film> implements FilmRepository {

    private final InDbFilmGenreRepository filmGenreRepository;
    private final InDbGenreRepository genreRepository;
    private final FilmExtractor filmExtractor;

//    private static final String FIND_BY_ID_QUERY = "SELECT * FROM film WHERE id = ?";

//    private static final String FIND_ALL_QUERY = "SELECT * FROM film";
    private static final String INSERT_QUERY = "INSERT INTO film (name, description, release_date, duration, rating_id) " +
            "VALUES (?, ?, ?, ?, ?)";
//    private static final String UPDATE_QUERY = "UPDATE film SET name = ?, description = ?, release_date = ?, duration = ?, rating_id = ? WHERE id = ?";

    private static final String INSERT_LIKE_QUERY = "INSERT INTO film_likes (film_id, user_id) VALUES (?, ?)";
    private static final String DELETE_LIKE_QUERY = "DELETE from film_likes WHERE  film_id = ? AND user_Id = ?";
    private static final String SELECT_POPULAR_QUERY = " SELECT f.*, COUNT(fl.film_id) AS likes " +
            "FROM film f JOIN film_likes fl ON f.id = fl.film_id " +
            "GROUP BY f.name ORDER BY likes DESC LIMIT ?";

    private static record FilmGenre (long filmId, long genreId) {}
    public InDbFilmRepository(JdbcTemplate jdbc, RowMapper<Film> mapper, InDbFilmGenreRepository filmGenreRepository, InDbGenreRepository genreRepository, FilmExtractor filmExtractor) {
        super(jdbc, mapper);
        this.filmGenreRepository = filmGenreRepository;
        this.genreRepository = genreRepository;
        this.filmExtractor = filmExtractor;
    }

    public Optional<Film> get(long filmId) {
        String sql = "SELECT * FROM film f JOIN mpa mpa ON f.RATING_ID = mpa.id " +
                "JOIN FILM_GENRE fg ON f.ID = fg.FILM_ID " +
                "JOIN GENRE g ON g.ID = fg.GENRE_ID WHERE f.id = ?";

        return Optional.ofNullable(jdbc.query(sql, filmExtractor, filmId));

//        return findOne(sql, filmId);
    }

    public List<Film> getAll() {
        String sql = "SELECT * FROM film f JOIN mpa mpa ON f.RATING_ID = mpa.id";
        String sql2 = "SELECT * FROM film_genre";

        List<Genre> genres = genreRepository.getAll();
        List<Film> films = findMany(sql);
        List<FilmGenre> filmGenres = jdbc.query(sql2, new FilmGenreRowMapper());

//        return findMany(FIND_ALL_QUERY);

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

    private static class FilmGenreRowMapper implements RowMapper<InDbFilmRepository.FilmGenre> {
        @Override
        public InDbFilmRepository.FilmGenre mapRow(ResultSet rs, int rowNum) throws SQLException {
            long filmId = rs.getLong("film_id");
            long genreId = rs.getLong("genre_id");
            return new InDbFilmRepository.FilmGenre(filmId, genreId);
        }
    }



    public Film save(Film film) {
        long id = insert(INSERT_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId()
        );
        film.setId(id);

        if (null != film.getGenres()) {
            filmGenreRepository.save(film);
        }
        return film;
    }

    public Film update(Film film) {
        String sql = "UPDATE film SET name = ?, description = ?, release_date = ?, duration = ?, rating_id = ? WHERE id = ?";
        update(
                sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()
        );
        if (null != film.getGenres()) {
            filmGenreRepository.delete(film);
            filmGenreRepository.save(film);
        }
        return get(film.getId()).orElseThrow();
    }

    public void addLike(Film film, User user) {
        insert(INSERT_LIKE_QUERY,
                film.getId(),
                user.getId()
        );
    }

    public boolean deleteLike(Film film, User user) {
        return delete(DELETE_LIKE_QUERY,
                film.getId(),
                user.getId()
        );
    }

    public List<Film> getPopular(long count) {
        return findMany(SELECT_POPULAR_QUERY, count);
    }

}
