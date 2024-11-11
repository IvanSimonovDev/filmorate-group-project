package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.FilmLike;
import ru.yandex.practicum.filmorate.repository.FilmLikeRepository;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class FilmLikeService {

    private final FilmLikeRepository filmLikeRepository;

    public List<FilmLike> getRecommendations(Long user_id) {
        List<FilmLike> recommendUserFilms = filmLikeRepository.getRecommendations(user_id);
        List<FilmLike> userFilms = filmLikeRepository.getFilmsUserById(user_id);
        recommendUserFilms.removeAll(userFilms);
        List<FilmLike> recommendFilms = new ArrayList<>();

        for (FilmLike indexFilm:recommendUserFilms) {
            recommendFilms.add((FilmLike) filmLikeRepository.getUserByFilmId(indexFilm.getFilm_id()));
        }
        return recommendFilms;
    }
}
