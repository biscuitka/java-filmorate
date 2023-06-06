INSERT INTO mpa (name)
VALUES('G'), ('PG'), ('PG-13'), ('R'), ('NC-17');

INSERT INTO users (name, login, email, birthday)
VALUES ('Jackie Chan','ChonWang', 'chan@ya.ru', '1954-04-07'),
('Bill Murray','DrPeterVenkman','murray@ya.ru','1950-09-21');

INSERT INTO films (name,description,release_date,duration,mpa_id)
VALUES
('Moonrise Kingdom','1960-е годы. Пара влюблённых подростков убегают из-под присмотра взрослых.','2012-05-16',90,3),
('Men in Black','Земля кишит пришельцами, за которыми глаз да глаз.','1997-06-25',98,3);

INSERT INTO film_likes (film_id, user_id)
VALUES
(1,1),
(2,1),
(2,2);
