DELETE
FROM user_roles;
DELETE
FROM meals;
DELETE
FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO meals (date_time, description, calories, user_id)
VALUES ('2021-02-27 12:34', 'Завтрак', 500, 100000),
       ('2021-02-27 15:34', 'Обед', 1000, 100000),
       ('2021-02-27 21:34', 'Ужин', 500, 100000),
       ('2021-02-27 12:34', 'Завтрак', 1000, 100001),
       ('2021-02-27 15:34', 'Обед', 1000, 100001),
       ('2021-02-27 22:34', 'Ужин', 500, 100001);
