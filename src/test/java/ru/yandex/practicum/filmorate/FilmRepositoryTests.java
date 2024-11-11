package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.inDatabase.*;
import ru.yandex.practicum.filmorate.repository.inDatabase.mapper.DirectorRowMapper;
import ru.yandex.practicum.filmorate.repository.inDatabase.mapper.FilmExtractor;
import ru.yandex.practicum.filmorate.repository.inDatabase.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.repository.inDatabase.mapper.GenreRowMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import({JdbcFilmRepository.class, FilmRowMapper.class, FilmExtractor.class, JdbcFilmGenreRepository.class,
        FilmGenreRowMapper.class, JdbcGenreRepository.class, GenreRowMapper.class, JdbcDirectorRepository.class,
        DirectorRowMapper.class, FilmDirectorRowMapper.class, JdbcFilmDirectorRepository.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmRepositoryTests {

    public static final long TEST_USER_ID = 2L;
    public static final long UPDATED_FILM_ID = 8L;
    public static final long TEST_NEWFILM_ID = 11L;
    public static final long TEST_FILM1_ID = 1L;
    @Autowired
    private final JdbcFilmRepository filmRepository;

    static Film getTestFilm1() {
        Film film = new Film();
        Mpa newMpa = new Mpa();
        newMpa.setId(3L);
        film.setId(TEST_FILM1_ID);
        film.setName("Inception");
        film.setDescription("A thief is given a chance to erase his criminal past.");
        film.setReleaseDate(LocalDate.of(2010, 7, 16));
        film.setDuration(148);
        film.setMpa(newMpa);
        return film;
    }

    static Film getNewFilm() {
        Film film = new Film();
        Mpa newMpa = new Mpa();
        newMpa.setId(2L);
        newMpa.setName("PG");
        Genre newGenre = new Genre();
        newGenre.setId(6L);
        newGenre.setName("Боевик");
        film.setId(TEST_NEWFILM_ID);
        film.setName("Comandos");
        film.setDescription("Saving the world.");
        film.setReleaseDate(LocalDate.of(1985, 6, 20));
        film.setDuration(90);
        film.setMpa(newMpa);
        film.setGenres(Set.of(newGenre));
        film.setDirectors(Set.of());
        return film;
    }

    static Film getUpdatedFilm() {
        Film film = new Film();
        Mpa mpa = new Mpa();
        mpa.setId(5L);
        Genre genre = new Genre();
        genre.setId(4L);
        film.setId(UPDATED_FILM_ID);
        film.setName("Gladiator 2");
        film.setDescription("A general seeks revenge against a corrupt emperor.");
        film.setReleaseDate(LocalDate.of(2010, 5, 5));
        film.setDuration(180);
        film.setMpa(mpa);
        film.setGenres(Set.of(genre));
        return film;
    }

    static User getTestUser() {
        User user = new User();
        user.setId(TEST_USER_ID);
        return user;
    }

    @DisplayName("Проверяем получение Фильма по ID")
    @Test
    public void shouldReturnFilmById() {
        Film film = filmRepository.get(TEST_FILM1_ID).orElseThrow();

        assertThat(film)
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(getTestFilm1());
    }

    @DisplayName("Проверяем получение всех фильмов")
    @Test
    public void shouldReturnAllFilms() {
        List<Film> films = filmRepository.getAll();

        assertThat(films)
                .isNotEmpty()
                .hasSize(10)
                .allMatch(Objects::nonNull);
    }

    @DisplayName("Проверяем добавление нового фильма, метод save")
    @Test
    public void shouldSaveNewFilm() {
        filmRepository.save(getNewFilm());
        List<Film> films = filmRepository.getAll();

        assertThat(films)
                .isNotEmpty()
                .hasSize(11)
                .filteredOn(film -> Objects.equals(film.getId(), getNewFilm().getId()))
                .first()
                .usingRecursiveComparison()
                .isEqualTo(getNewFilm());
    }

    @DisplayName("Проверяем обновление данных существующего фильма, метод update")
    @Test
    public void shouldUpdateFilm() {
        filmRepository.update(getUpdatedFilm());
        Optional<Film> optionalFilm = filmRepository.get(getUpdatedFilm().getId());

        assertThat(optionalFilm)
                .isPresent()
                .hasValueSatisfying(film -> {
                    assertThat(film).hasFieldOrPropertyWithValue("id", getUpdatedFilm().getId());
                    assertThat(film).hasFieldOrPropertyWithValue("name", getUpdatedFilm().getName());
                });
    }

    @DisplayName("Проверяем удаление like для фильма")
    @Test
    public void shouldDeleteLike() {
        boolean delLike = filmRepository.deleteLike(getTestFilm1(), getTestUser());

        assertThat(delLike).isNotEqualTo(false);
    }

    @DisplayName("Проверяем получение списка популярных фильмов")
    @Test
    public void shouldGetPopularFilms() {
        List<Film> popular = filmRepository.getPopular(2);

        assertThat(popular)
                .isNotEmpty()
                .hasSize(2)
                .allMatch(Objects::nonNull)
                .extracting(Film::getName)
                .contains("Inception", "The Matrix");
    }
}