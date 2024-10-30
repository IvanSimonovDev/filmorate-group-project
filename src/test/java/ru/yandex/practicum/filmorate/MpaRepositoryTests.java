package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.inDatabase.JdbcMpaRepository;
import ru.yandex.practicum.filmorate.repository.inDatabase.mapper.MpaRowMapper;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import({JdbcMpaRepository.class, MpaRowMapper.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MpaRepositoryTests {

    private final JdbcMpaRepository mpaRepository;
    public static final long MPA_ID = 1L;
    public static final long MPA_ID2 = 5L;
    public static final String MPA_NAME = "G";
    public static final String MPA_NAME2 = "NC-17";

    static Mpa getTestMpa() {
        Mpa mpa = new Mpa();
        mpa.setId(MPA_ID);
        mpa.setName(MPA_NAME);
        return mpa;
    }

    static Mpa getTestMpa2() {
        Mpa mpa = new Mpa();
        mpa.setId(MPA_ID2);
        mpa.setName(MPA_NAME2);
        return mpa;
    }

    @DisplayName("Проверяем получение всех записей из таблицы mpa")
    @Test
    public void shouldGetAllMpa() {
        List<Mpa> mpaAll = mpaRepository.getAll();

        assertThat(mpaAll)
                .isNotEmpty()
                .hasSizeGreaterThanOrEqualTo(5)
                .allMatch(Objects::nonNull)
                .extracting(Mpa::getName)
                .contains(getTestMpa().getName(), getTestMpa2().getName());
    }

    @DisplayName("Проверяем получение одной записи из таблицы mpa")
    @Test
    public void shouldGetMpaById() {
        Mpa mpa = mpaRepository.getById(getTestMpa().getId()).orElseThrow();

        assertThat(mpa)
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(getTestMpa());
    }

}