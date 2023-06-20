package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
@Primary
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film createFilm(Film film) {
        String sqlInsertGenres = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)";

        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films").usingGeneratedKeyColumns("film_id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", film.getName());
        parameters.put("description", film.getDescription());
        parameters.put("release_date", Date.valueOf(film.getReleaseDate()));
        parameters.put("duration", film.getDuration());
        parameters.put("mpa_id", film.getMpa().getId());
        film.setId(insert.executeAndReturnKey(parameters).longValue());

        List<Object[]> batchParameters = new ArrayList<>();
        for (Genre genre : film.getGenres()) {
            Object[] genreParameters = {film.getId(), genre.getId()};
            batchParameters.add(genreParameters);
        }
        jdbcTemplate.batchUpdate(sqlInsertGenres, batchParameters);

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
        List<Object[]> batchParameters = new ArrayList<>();
        for (Genre genre : film.getGenres()) {
            Object[] genreParameters = {film.getId(), genre.getId()};
            batchParameters.add(genreParameters);
        }
        jdbcTemplate.batchUpdate(sqlInsertGenres, batchParameters);

        return film;
    }

    @Override
    public void deleteFilmById(long id) {
        jdbcTemplate.update("DELETE FROM films WHERE film_id = ?", id);
    }

    @Override
    public List<Film> getFilms() {
        String sql = "SELECT f.film_id, " +
                "f.name AS title, " +
                "f.description, " +
                "f.release_date, " +
                "f.duration, " +
                "f.mpa_id, " +
                "m.name AS mpa, " +
                "GROUP_CONCAT(g.genre_id) AS genre_ids, " +
                "GROUP_CONCAT(g.name) AS genre_names " +
                "FROM films AS f " +
                "JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
                "LEFT JOIN film_genre AS fg ON f.film_id = fg.film_id " +
                "LEFT JOIN genres AS g ON fg.genre_id = g.genre_id " +
                "GROUP BY f.film_id";

        return jdbcTemplate.query(sql, new FilmMapperWithMpaGenre());

    }

    public List<Film> getFilmsByIdList(List<Long> ids) {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        String sql = "SELECT f.film_id, " +
                "f.name AS title, " +
                "f.description, " +
                "f.release_date, " +
                "f.duration, " +
                "f.mpa_id, " +
                "m.name AS mpa, " +
                "FROM films AS f JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
                "WHERE film_id IN (:ids)";

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("ids", ids);
        return namedParameterJdbcTemplate.query(sql, parameters, new FilmMapper());
    }

    @Override
    public List<Film> getCommonFilms(long userId, long friendId) {
        /*String sql = "SELECT film_id " +
                "FROM film_likes " +
                "WHERE user_id = ? " +
                "INTERSECT SELECT film_id " +
                "FROM film_likes " +
                "WHERE user_id = ?" +
                "GROUP BY user_id";*/
        String sql = "SELECT f.film_id, f.name AS title, f.description, f.release_date, f.duration, f.mpa_id, m.name AS mpa, " +
                "GROUP_CONCAT(g.genre_id) AS genre_ids, GROUP_CONCAT(g.name) AS genre_names " +
                "FROM film_likes AS fl " +
                "JOIN films AS f ON fl.film_id = f.film_id " +
                "JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
                "LEFT JOIN film_genre AS fg ON f.film_id = fg.film_id " +
                "LEFT JOIN genres AS g ON fg.genre_id = g.genre_id " +
                "WHERE fl.user_id = ? AND f.film_id IN ( " +
                "  SELECT film_id FROM film_likes WHERE user_id = ? " +
                ") " +
                "GROUP BY f.film_id";
        return jdbcTemplate.query(sql, new FilmMapperWithMpaGenre(), userId, friendId);
    }

    @Override
    public Film getFilmById(long id) {
        String sql = "SELECT f.film_id, " +
                "f.name AS title, " +
                "f.description, " +
                "f.release_date, " +
                "f.duration, " +
                "f.mpa_id, " +
                "m.name AS mpa, " +
                "GROUP_CONCAT(g.genre_id) AS genre_ids, " +
                "GROUP_CONCAT(g.name) AS genre_names " +
                "FROM films AS f " +
                "JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
                "LEFT JOIN film_genre AS fg ON f.film_id = fg.film_id " +
                "LEFT JOIN genres AS g ON fg.genre_id = g.genre_id " +
                "WHERE f.film_id = ? " +
                "GROUP BY f.film_id ";
        try {
            return jdbcTemplate.queryForObject(sql, new FilmMapperWithMpaGenre(), id);
        } catch (EmptyResultDataAccessException exp) {
            log.error("Ошибка при запросе по id {}" + exp.getMessage(), id);
            throw new NotFoundException("Фильм с id " + id + " не найден");
        }
    }

    private class FilmMapperWithMpaGenre implements RowMapper<Film> {
        @Override
        public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
            Film film = new Film();
            film.setId(rs.getLong("film_id"));
            film.setName(rs.getString("title"));
            film.setDescription(rs.getString("description"));
            film.setReleaseDate(rs.getDate("release_date").toLocalDate());
            film.setDuration(rs.getLong("duration"));

            Mpa mpa = new Mpa();
            mpa.setId(rs.getInt("mpa_id"));
            mpa.setName(rs.getString("mpa"));
            film.setMpa(mpa);

            String genreIds = rs.getString("genre_ids");
            String genreNames = rs.getString("genre_names");
            if (genreIds != null && genreNames != null) {
                String[] idArray = genreIds.split(",");
                String[] nameArray = genreNames.split(",");
                LinkedHashSet<Genre> genres = new LinkedHashSet<>();
                for (int i = 0; i < idArray.length; i++) {
                    Genre genre = new Genre();
                    genre.setId(Integer.parseInt(idArray[i]));
                    genre.setName(nameArray[i]);
                    genres.add(genre);
                }
                film.setGenres(genres);
            }

            return film;
        }
    }
}
