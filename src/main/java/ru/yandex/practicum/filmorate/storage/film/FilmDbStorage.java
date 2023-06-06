package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genres.GenresStorage;
import ru.yandex.practicum.filmorate.storage.likes.LikesStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
@Primary
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final MpaStorage mpaStorage;
    private final GenresStorage genresStorage;
    private final LikesStorage likesStorage;

    @Override
    public Film createFilm(Film film) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films").usingGeneratedKeyColumns("film_id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", film.getName());
        parameters.put("description", film.getDescription());
        parameters.put("release_date", Date.valueOf(film.getReleaseDate()));
        parameters.put("duration", film.getDuration());
        parameters.put("mpa_id", film.getMpa().getId());
        film.setId(insert.executeAndReturnKey(parameters).longValue());
        String sqlInsertGenres = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)";
        for (Genre genre : film.getGenres()) {
            jdbcTemplate.update(sqlInsertGenres, film.getId(), genre.getId());
        }

        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        String sqlSelect = "SELECT film_id FROM films WHERE film_id = ?";
        try {
            jdbcTemplate.queryForObject(sqlSelect, Long.class, film.getId());
        } catch (EmptyResultDataAccessException exp) {
            log.error("Ошибка при обновлении по id {} " + exp.getMessage(), film.getId());
            throw new NotFoundException("Фильм с id " + film.getId() + " не найден");
        }
        String sqlUpdate = "UPDATE films " +
                "SET name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ? " +
                "WHERE film_id = ?";
        jdbcTemplate.update(
                sqlUpdate,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()
        );
        String sqlDeleteGenres = "DELETE FROM film_genre WHERE film_id = ?";
        jdbcTemplate.update(sqlDeleteGenres, film.getId());

        String sqlInsertGenres = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)";
        for (Genre genre : film.getGenres()) {
            jdbcTemplate.update(sqlInsertGenres, film.getId(), genre.getId());
        }
        return film;
    }

    @Override
    public void deleteFilmById(long id) {
        String sql = "DELETE " +
                "FROM films " +
                "WHERE film_id = ?";
        try {
            jdbcTemplate.update(sql, id);
        } catch (EmptyResultDataAccessException exp) {
            log.error("Ошибка при удалении по id {}" + exp.getMessage(), id);
            throw new NotFoundException("Фильм с id " + id + " не найден");
        }
    }

    @Override
    public List<Film> getFilms() {
        String sql = "SELECT film_id, " +
                "name, " +
                "description, " +
                "release_date, " +
                "duration, " +
                "mpa_id " +
                "FROM films";

        List<Film> films = jdbcTemplate.query(sql, new FilmMapper());
        for (Film film : films) {
            film.setMpa(mpaStorage.getByFilmId(film.getId()));
            for (Long likedUserId : likesStorage.getLikesFromUsers(film.getId())) {
                film.getLikes().add(likedUserId);
            }
            for (Integer genreId : genresStorage.getGenresByFilm(film.getId())) {
                film.getGenres().add(genresStorage.getById(genreId));
            }
        }
        return films;
    }

    @Override
    public Film getFilmById(long id) {
        String sql = "SELECT film_id, " +
                "name, " +
                "description, " +
                "release_date, " +
                "duration, " +
                "mpa_id " +
                "FROM films " +
                "WHERE film_id = ?";
        try {
            Film film = jdbcTemplate.queryForObject(sql, new FilmMapper(), id);
            if (film != null) {
                film.setMpa(mpaStorage.getByFilmId(film.getId()));
                for (Long likedUserId : likesStorage.getLikesFromUsers(film.getId())) {
                    film.getLikes().add(likedUserId);
                }
                for (Integer genreId : genresStorage.getGenresByFilm(film.getId())) {
                    film.getGenres().add(genresStorage.getById(genreId));
                }
            }
            return film;

        } catch (EmptyResultDataAccessException exp) {
            log.error("Ошибка при запросе по id {}" + exp.getMessage(), id);
            throw new NotFoundException("Фильм с id " + id + " не найден");
        }
    }
}
