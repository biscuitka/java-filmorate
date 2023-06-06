INSERT INTO users (name, login, email, birthday)
VALUES ('Jackie Chan','ChonWang', 'chan@ya.ru', '1954-04-07'),
('Bill Murray','DrPeterVenkman','murray@ya.ru','1950-09-21'),
('Jack Choo','Chang', 'jj@ya.ru', '1994-04-05'),
('Bella Morrly','Bee','bee@ya.ru','1999-01-21');

INSERT INTO friends (user_id, friend_id)
VALUES
(1, 2),
(2, 1),
(1, 3),
(3, 1),
(3, 4),
(4, 3);