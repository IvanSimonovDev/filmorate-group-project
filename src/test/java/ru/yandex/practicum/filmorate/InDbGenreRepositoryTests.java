package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.inDatabase.InDbGenreRepository;
import ru.yandex.practicum.filmorate.repository.inDatabase.mapper.GenreRowMapper;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import({InDbGenreRepository.class, GenreRowMapper.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class InDbGenreRepositoryTests {

    private final InDbGenreRepository genreRepository;

    public static final long GENRE_ID = 1L;
    public static final long GENRE_ID2 = 5L;
    public static final String GENRE_NAME = "Комедия";
    public static final String GENRE_NAME2 = "Документальный";

    static Genre getTestGenre() {
        Genre genre = new Genre();
        genre.setId(GENRE_ID);
        genre.setName(GENRE_NAME);
        return genre;
    }

    static Genre getTestGenre2() {
        Genre genre = new Genre();
        genre.setId(GENRE_ID2);
        genre.setName(GENRE_NAME2);
        return genre;
    }

    @DisplayName("Проверяем получение всех записей из таблицы Жанров(genre)")
    @Test
    public void shouldGetAllGenre() {
        List<Genre> genres = genreRepository.getAll();

        assertThat(genres)
                .isNotEmpty()
                .hasSizeGreaterThanOrEqualTo(6)
                .allMatch(Objects::nonNull)
                .extracting(Genre::getName)
                .contains(getTestGenre().getName(), getTestGenre2().getName());
    }

    @DisplayName("Проверяем получение одной записи из таблицы Жанров(genre)")
    @Test
    public void shouldGetGenreById() {
        Genre genre = genreRepository.getById(getTestGenre().getId()).orElseThrow();

        assertThat(genre)
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(getTestGenre());
    }

}