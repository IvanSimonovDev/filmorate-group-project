package ru.yandex.practicum.filmorate.repository.inDatabase;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.inDatabase.mapper.FilmExtractor;

import java.util.*;
import java.util.stream.Collectors;

@Primary
@Repository
public class JdbcFilmRepository extends JdbcBaseRepository<Film> implements FilmRepository {

    private final FilmGenreRowMapper filmGenreRowMapper;
    private final JdbcFilmGenreRepository filmGenreRepository;
    private final JdbcGenreRepository genreRepository;
    private final FilmDirectorRowMapper filmDirectorRowMapper;
    private final JdbcFilmDirectorRepository filmDirectorRepository;
    private final JdbcDirectorRepository directorRepository;
    private final FilmExtractor filmExtractor;

    protected record FilmGenre(long filmId, long genreId) {
    }

    protected record FilmDirector(long filmId, long directorId) {
    }

    public JdbcFilmRepository(NamedParameterJdbcOperations jdbc, RowMapper<Film> mapper, JdbcFilmGenreRepository filmGenreRepository,
                              JdbcGenreRepository genreRepository, FilmExtractor filmExtractor, FilmGenreRowMapper filmGenreRowMapper,
                              JdbcDirectorRepository directorRepository, FilmDirectorRowMapper filmDirectorRowMapper,
                              JdbcFilmDirectorRepository filmDirectorRepository) {
        super(jdbc, mapper);
        this.filmGenreRepository = filmGenreRepository;
        this.genreRepository = genreRepository;
        this.filmExtractor = filmExtractor;
        this.filmGenreRowMapper = filmGenreRowMapper;
        this.directorRepository = directorRepository;
        this.filmDirectorRowMapper = filmDirectorRowMapper;
        this.filmDirectorRepository = filmDirectorRepository;
    }

    public Optional<Film> get(long filmId) {
        String sql = "SELECT * FROM film f JOIN mpa mpa ON f.RATING_ID = mpa.id " +
                "LEFT JOIN FILM_GENRE fg ON f.ID = fg.FILM_ID " +
                "LEFT JOIN GENRE g ON g.ID = fg.GENRE_ID " +
                "LEFT JOIN FILM_DIRECTOR fd ON f.ID = fd.FILM_ID " +
                "LEFT JOIN DIRECTORS d ON d.ID = fd.DIRECTOR_ID " +
                " WHERE f.id = :filmId";

        Map<String, Object> params = Map.of("filmId", filmId);

        return Optional.ofNullable(jdbc.query(sql, params, filmExtractor));
    }

    public List<Film> getAll() {
        String sql1 = "SELECT * FROM film f JOIN mpa mpa ON f.RATING_ID = mpa.id";
        String sql2 = "SELECT * FROM film_director";

        List<FilmDirector> filmDirectors = jdbc.query(sql2, filmDirectorRowMapper);
        List<Director> directors = directorRepository.getAll();

        List<Film> films = findMany(sql1, Collections.emptyMap());

        fillGenres(films);
        return fillUpDirectors(filmDirectors, directors, films);
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

        if (null != film.getDirectors()) {
            filmDirectorRepository.save(film);
        }
        return film;
    }

    public Film update(Film film) {
        String sql = "UPDATE film SET name = :name, description = :description, release_date = :release_date, duration = :duration, " +
                "rating_id = :rating_id WHERE id = :id";
        Map<String, Object> params = Map.of("name", film.getName(), "description", film.getDescription(), "release_date", film.getReleaseDate(),
                "duration", film.getDuration(), "rating_id", film.getMpa().getId(), "id", film.getId());

        update(sql, params);

        if (null != film.getGenres()) {
            filmGenreRepository.delete(film);
            filmGenreRepository.save(film);
        }
        if (null != film.getDirectors()) {
            filmDirectorRepository.delete(film);
            filmDirectorRepository.save(film);
        }
        return film;
    }

    public void delete(long filmId) {
        String sql = "DELETE from film WHERE id = :filmId";
        Map<String, Object> params = Map.of("filmId", filmId);

        delete(sql, params);
    }

    public List<Film> getSortedDirectorsFilms(long directorId, String sortBy) {

        String sql = "SELECT f.*, mpa.ID, MPA.NAME, COUNT(fl.film_id) AS likes, d.ID, d.NAME  " +
                "FROM film f JOIN mpa mpa ON f.RATING_ID = mpa.id " +
                "LEFT JOIN film_likes fl ON f.id = fl.film_id " +
                "LEFT JOIN FILM_DIRECTOR fd ON f.ID = fd.FILM_ID " +
                "LEFT JOIN DIRECTORS d ON d.ID = fd.DIRECTOR_ID " +
                "WHERE d.ID = :directorId " +
                "GROUP BY f.id, f.name, f.description, f.release_date, f.duration, mpa.ID, mpa.NAME, d.ID, d.NAME " +
                "ORDER BY " + sortBy;

        String sql2 = "SELECT * FROM film_director";

        Map<String, Object> params = Map.of("directorId", directorId, "sortBy", (sortBy));

        List<Film> films = findMany(sql, params);
        List<FilmDirector> filmDirectors = jdbc.query(sql2, filmDirectorRowMapper);
        List<Director> directors = directorRepository.getAll();

        return fillUpDirectors(filmDirectors, directors, films);
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

    @Override
    public Collection<Film> search(String query, String by) {
        String sql1 = "SELECT * FROM film f JOIN mpa mpa ON f.RATING_ID = mpa.id";
        String sql2 = "SELECT * FROM film_genre";
        String sql3 = "SELECT * FROM film_director";

        List<FilmDirector> filmDirectors = jdbc.query(sql3, filmDirectorRowMapper);

        List<Film> foundByDirector = new ArrayList<>();
        if (by.contains("director")) {
            List<Director> filteredDirectors = directorRepository.findBySubstringName(query);
            filteredDirectors.forEach(director -> {
                List<Film> films = getDirectorsFilms(director.getId());
                foundByDirector.addAll(films);
            });
        }

        List<Film> foundByTitle = new ArrayList<>();
        if (by.contains("title")) {

            List<Film> filteredFilms = findMany(
                    sql1 + " WHERE LOWER(f.name) LIKE LOWER('%" + query + "%')",
                    Collections.emptyMap());

            List<Director> allDirectors = directorRepository.getAll();
            foundByTitle = fillUpDirectors(filmDirectors, allDirectors, filteredFilms);
        }
        foundByTitle.addAll(foundByDirector);

        List<FilmGenre> filmGenres = jdbc.query(sql2, filmGenreRowMapper);
        List<Genre> genres = genreRepository.getAll();
        foundByTitle.forEach(film -> {
            Set<Genre> associatedGenres = filmGenres.stream()
                    .filter(fg -> fg.filmId() == film.getId())
                    .flatMap(fg -> genres.stream()
                            .filter(genre -> genre.getId() == fg.genreId()))
                    .collect(Collectors.toSet());
            film.setGenres(associatedGenres);
        });

        return foundByTitle;
    }

    public List<Film> getDirectorsFilms(long directorId) {
        String sql = "SELECT f.*, mpa.ID, MPA.NAME, COUNT(fl.film_id) AS likes, d.ID, d.NAME  " +
                "FROM film f JOIN mpa mpa ON f.RATING_ID = mpa.id " +
                "LEFT JOIN film_likes fl ON f.id = fl.film_id " +
                "LEFT JOIN FILM_DIRECTOR fd ON f.ID = fd.FILM_ID " +
                "LEFT JOIN DIRECTORS d ON d.ID = fd.DIRECTOR_ID " +
                "WHERE d.ID = :directorId " +
                "GROUP BY f.id, f.name, f.description, f.release_date, f.duration, mpa.ID, mpa.NAME, d.ID, d.NAME";

        String sql2 = "SELECT * FROM film_director";

        Map<String, Object> params = Map.of("directorId", directorId);

        List<Film> films = findMany(sql, params);
        List<FilmDirector> filmDirectors = jdbc.query(sql2, filmDirectorRowMapper);
        List<Director> directors = directorRepository.getAll();
        return fillUpDirectors(filmDirectors, directors, films);
    }

    public List<Film> getCommonFilms(Long userId1, Long userId2) {
        String sql1 = "SELECT * FROM film f JOIN mpa mpa ON f.rating_id = mpa.id" +
                " WHERE f.id IN (SELECT fl1.film_id FROM film_likes fl1 " +
                "INNER JOIN film_likes fl2 ON fl1.film_id = fl2.film_id " +
                "WHERE fl1.user_id = :userId1 AND fl2.user_id = :userId2)";

        String sql2 = "SELECT * FROM film_director";

        Map<String, Object> params = Map.of("userId1", userId1, "userId2", userId2);

        List<FilmDirector> filmDirectors = jdbc.query(sql2, filmDirectorRowMapper);
        List<Director> directors = directorRepository.getAll();

        List<Film> films = findMany(sql1, params);

        fillGenres(films);

        return fillUpDirectors(filmDirectors, directors, films);
    }

    private List<Film> fillUpDirectors
            (List<FilmDirector> filmDirectors, List<Director> directors, List<Film> films) {
        films.forEach(film -> {
            Set<Director> associatedDirector = filmDirectors.stream()
                    .filter(fd -> fd.filmId() == film.getId())
                    .flatMap(fd -> directors.stream()
                            .filter(director -> director.getId() == fd.directorId()))
                    .collect(Collectors.toSet());

            film.setDirectors(associatedDirector);
        });
        return films;
    }

    private void fillGenres(List<Film> films) {
        String sql = "SELECT * FROM film_genre";

        List<FilmGenre> filmGenres = jdbc.query(sql, filmGenreRowMapper);
        List<Genre> genres = genreRepository.getAll();
        films.forEach(film -> {
            Set<Genre> associatedGenres = filmGenres.stream()
                    .filter(fg -> fg.filmId() == film.getId())
                    .flatMap(fg -> genres.stream()
                            .filter(genre -> genre.getId() == fg.genreId()))
                    .collect(Collectors.toSet());
            film.setGenres(associatedGenres);
        });
    }
}
