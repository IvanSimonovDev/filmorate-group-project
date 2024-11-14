-- Сбрасываем счетчики автоинкремента для таблиц с полем id
ALTER TABLE users ALTER COLUMN id RESTART WITH 1;
ALTER TABLE film ALTER COLUMN id RESTART WITH 1;
ALTER TABLE genre ALTER COLUMN id RESTART WITH 1;
ALTER TABLE mpa ALTER COLUMN id RESTART WITH 1;
ALTER TABLE directors ALTER COLUMN id RESTART WITH 1;
ALTER TABLE reviews ALTER COLUMN reviewId RESTART WITH 1;
ALTER TABLE events ALTER COLUMN id RESTART WITH 1;

-- Наполнение тестовыми данными
INSERT INTO mpa (name, description) VALUES
                                           ('G', 'General audiences.'),
                                           ('PG', 'Parental guidance suggested.'),
                                           ('PG-13', 'Parents are strongly cautioned.'),
                                           ('R', 'Restricted.'),
                                           ('NC-17', 'Adults only.');

INSERT INTO genre (name) VALUES
                             ('Комедия'),
                             ('Драма'),
                             ('Мультфильм'),
                             ('Триллер'),
                             ('Документальный'),
                             ('Боевик');
