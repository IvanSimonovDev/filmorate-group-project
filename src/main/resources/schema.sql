-- Удаление таблиц по порядку зависимости
DROP TABLE IF EXISTS reviews_likes_dislikes;
DROP TABLE IF EXISTS reviews;
DROP TABLE IF EXISTS film_director;        -- Удаляем лайки фильмов
DROP TABLE IF EXISTS film_likes;        -- Удаляем лайки фильмов
DROP TABLE IF EXISTS film_genre;       -- Удаляем жанры фильмов
DROP TABLE IF EXISTS user_friend;     -- Удаляем дружеские связи пользователей
DROP TABLE IF EXISTS film;             -- Удаляем фильмы
DROP TABLE IF EXISTS genre;            -- Удаляем жанры
DROP TABLE IF EXISTS mpa;           -- Удаляем рейтинги
DROP TABLE IF EXISTS directors;            -- Удаляем режиссеров
DROP TABLE IF EXISTS events;            -- Удаляем события
DROP TABLE IF EXISTS users;            -- Удаляем пользователей

CREATE TABLE IF NOT EXISTS mpa (
                                      id INTEGER PRIMARY KEY AUTO_INCREMENT,
                                      name VARCHAR(255),
                                      description VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS genre (
                                     id INTEGER PRIMARY KEY AUTO_INCREMENT,
                                     name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS directors (
                                     id INTEGER PRIMARY KEY AUTO_INCREMENT,
                                     name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS film (
                                    id INTEGER PRIMARY KEY AUTO_INCREMENT,
                                    name VARCHAR(255),
                                    description VARCHAR(255),
                                    release_date DATE,
                                    duration INTEGER,
                                    rating_id INTEGER,
                                    CONSTRAINT fk_film_rating FOREIGN KEY (rating_id) REFERENCES mpa (id)
);

CREATE TABLE IF NOT EXISTS film_genre (
                                          film_id INTEGER,
                                          genre_id INTEGER,
                                          PRIMARY KEY (film_id, genre_id),
                                          CONSTRAINT fk_film_id FOREIGN KEY (film_id) REFERENCES film (id)  ON DELETE CASCADE,
                                          CONSTRAINT fk_genre_id FOREIGN KEY (genre_id) REFERENCES genre (id)
);

CREATE TABLE IF NOT EXISTS film_director (
                                          film_id INTEGER,
                                          director_id INTEGER,
                                          PRIMARY KEY (film_id, director_id),
                                          CONSTRAINT fk_fd_film_id FOREIGN KEY (film_id) REFERENCES film (id)  ON DELETE CASCADE,
                                          CONSTRAINT fk_director_id FOREIGN KEY (director_id) REFERENCES directors (id)
);

CREATE TABLE IF NOT EXISTS users (
                                      id INTEGER PRIMARY KEY AUTO_INCREMENT,
                                      email VARCHAR(255),
                                      login VARCHAR(255),
                                      name VARCHAR(255),
                                      birthday DATE
);

CREATE TABLE IF NOT EXISTS film_likes (
                                          film_id INTEGER,
                                          user_id INTEGER,
                                          PRIMARY KEY (film_id, user_id),
                                          CONSTRAINT fk_film_likes_id FOREIGN KEY (film_id) REFERENCES film (id) ON DELETE CASCADE,
                                          CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS user_friend (
                                            user_id INTEGER,
                                            friend_id INTEGER,
                                            isConfirmed BOOLEAN,
                                            PRIMARY KEY (user_id, friend_id),
                                            CONSTRAINT fk_user_id_friend FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
                                            CONSTRAINT fk_friend_id FOREIGN KEY (friend_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS reviews (
                                        reviewId INTEGER PRIMARY KEY AUTO_INCREMENT,
                                        content TEXT NOT NULL,
                                        isPositive BOOLEAN NOT NULL,
                                        userId INTEGER,
                                        filmId INTEGER,
                                        CONSTRAINT fk_user_review FOREIGN KEY (userId) REFERENCES users (id) ON DELETE CASCADE,
                                        CONSTRAINT fk_film_review FOREIGN KEY (filmId) REFERENCES film (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS reviews_likes_dislikes (
                                                       id INTEGER PRIMARY KEY AUTO_INCREMENT,
                                                       reviewId INTEGER,
                                                       userId INTEGER,
                                                       val INTEGER NOT NULL,
                                                       CONSTRAINT fk_user_ld FOREIGN KEY (userId) REFERENCES users(id) ON DELETE CASCADE,
                                                       CONSTRAINT fk_review_ld FOREIGN KEY (reviewId) REFERENCES reviews(reviewId) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS events (
                                    id INTEGER PRIMARY KEY AUTO_INCREMENT,
                                    user_id INTEGER,
                                    timestamp BIGINT,
                                    event_type VARCHAR(255),
                                    operation VARCHAR(255),
                                    entity_id INTEGER NOT NULL,
                                    CONSTRAINT fk_user_events FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
