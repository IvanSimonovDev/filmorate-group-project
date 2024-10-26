-- -- Очистка таблиц по порядку зависимости
-- DELETE FROM film;             -- Удаляем фильмы
-- DELETE FROM genre;            -- Удаляем жанры
-- DELETE FROM mpa;           -- Удаляем рейтинги
-- DELETE FROM users;            -- Удаляем пользователей

-- -- Сбрасываем счетчики автоинкремента для таблиц с полем id
-- ALTER TABLE users ALTER COLUMN id RESTART WITH 1;
-- ALTER TABLE film ALTER COLUMN id RESTART WITH 1;
-- ALTER TABLE genre ALTER COLUMN id RESTART WITH 1;
-- ALTER TABLE mpa ALTER COLUMN id RESTART WITH 1;

-- Наполнение тестовыми данными
INSERT INTO mpa (name, description) VALUES
                                           ('G', 'General audiences.'),
                                           ('PG', 'Parental guidance suggested.'),
                                           ('PG-13', 'Parents are strongly cautioned.'),
                                           ('R', 'Restricted.'),
                                           ('NC-17', 'Adults only.');

INSERT INTO film (name, description, release_date, duration, rating_id) VALUES
                                                                            ('Inception', 'A thief is given a chance to erase his criminal past.', '2010-07-16', 148, 3),
                                                                            ('The Matrix', 'A hacker learns the shocking truth about reality.', '1999-03-31', 136, 4),
                                                                            ('Interstellar', 'Explorers travel through a wormhole to find a new home.', '2014-11-07', 169, 3),
                                                                            ('The Shawshank Redemption', 'Two men bond and find solace in prison.', '1994-09-23', 142, 4),
                                                                            ('The Godfather', 'A crime boss hands control to his reluctant son.', '1972-03-24', 175, 4),
                                                                            ('Pulp Fiction', 'Mob hitmen and a boxer navigate intertwined lives.', '1994-10-14', 154, 4),
                                                                            ('Forrest Gump', 'A man witnesses historic events through his life.', '1994-07-06', 142, 3),
                                                                            ('Gladiator', 'A general seeks revenge against a corrupt emperor.', '2000-05-05', 155, 4),
                                                                            ('The Dark Knight', 'The Joker brings chaos to Gotham City.', '2008-07-18', 152, 3),
                                                                            ('The Lion King', 'A young lion returns to reclaim his throne.', '1994-06-15', 88, 1);

INSERT INTO genre (name) VALUES
                             ('Комедия'),
                             ('Драма'),
                             ('Мультфильм'),
                             ('Триллер'),
                             ('Документальный'),
                             ('Боевик');


INSERT INTO users (email, login, name, birthday) VALUES
                                                     ('TestUser1@example.com', 'TestUser1', 'Bober Kurva', '1990-05-15'),
                                                     ('TestUser2@example.com', 'TestUser2', 'John Doe', '1991-01-10'),
                                                     ('TestUser3@example.com', 'TestUser3', 'Test User3', '2000-01-10'),
                                                     ('TestUser4@example.com', 'TestUser3', 'Test User4', '2005-01-10');


INSERT INTO film_likes (film_id, user_id) VALUES
                                              (1, 1),  -- User 1 likes Inception
                                              (2, 2),  -- User 2 likes The Matrix
                                              (1, 2);

INSERT INTO user_friend (user_id, friend_id, isConfirmed) VALUES
                                                              (1, 2, false),
                                                              (1, 3, false),
                                                              (2, 3, false),
                                                              (2, 4, false);
