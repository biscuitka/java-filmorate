DROP TABLE IF EXISTS mpa CASCADE;
DROP TABLE IF EXISTS genres CASCADE;
DROP TABLE IF EXISTS film_genre CASCADE;
DROP TABLE IF EXISTS films CASCADE;
DROP TABLE IF EXISTS film_likes CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS friends CASCADE;


CREATE TABLE IF NOT EXISTS mpa (
  mpa_id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  name varchar(200)
);

CREATE TABLE IF NOT EXISTS genres (
  genre_id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  name varchar(200) NOT NULL
);

CREATE TABLE IF NOT EXISTS films (
  film_id long GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  name varchar(200) NOT NULL,
  description varchar(200),
  release_date date,
  duration long,
  mpa_id int NOT NULL REFERENCES mpa(mpa_id) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS film_genre (
  film_id long REFERENCES films(film_id) ON DELETE CASCADE,
  genre_id int REFERENCES genres(genre_id) ON DELETE RESTRICT,
  PRIMARY KEY (film_id, genre_id)
);

CREATE TABLE IF NOT EXISTS users (
  user_id long GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  name varchar NOT NULL,
  login varchar,
  email varchar NOT NULL,
  birthday date
);

CREATE TABLE IF NOT EXISTS film_likes (
  film_id long REFERENCES films(film_id) ON DELETE CASCADE,
  user_id long REFERENCES users(user_id) ON DELETE CASCADE,
  CONSTRAINT PK_film_likes PRIMARY KEY (film_id, user_id)
);

CREATE TABLE IF NOT EXISTS friends (
  user_id long REFERENCES users(user_id) ON DELETE CASCADE,
  friend_id long REFERENCES users(user_id) ON DELETE CASCADE,
  friendship_status boolean,
  CONSTRAINT PK_friends PRIMARY KEY (user_id, friend_id)
);