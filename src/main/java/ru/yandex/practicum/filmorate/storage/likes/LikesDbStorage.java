package ru.yandex.practicum.filmorate.storage.likes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
@Primary
public class LikesDbStorage implements LikesStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addLike(long filmId, long userId) {
        String sql = "INSERT INTO film_likes (film_id, user_id) " +
                "VALUES (?, ?)";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public void deleteLike(long filmId, long userId) {
        String sqlDelete = "DELETE FROM film_likes " +
                "WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sqlDelete, filmId, userId);
    }

    @Override
    public List<Long> getLikesFromUsers(long filmId) {
        String sql = "SELECT user_id FROM film_likes WHERE film_id = ?";
        return jdbcTemplate.queryForList(sql, Long.class, filmId);
    }

    @Override
    public List<Long> getMostLikedFilms(int count) {
        String sql = "SELECT f.film_id " +
                "FROM films AS f LEFT JOIN film_likes AS fl ON fl.film_id = f.film_id " +
                "GROUP BY f.film_id " +
                "ORDER BY COUNT(fl.user_id) DESC " +
                "LIMIT ?";
        return jdbcTemplate.queryForList(sql, Long.class, count);
    }

    public List<Long> getLikesByFilms(List<Film> films) {
        List<Long> filmIds = new ArrayList<>();
        for (Film film : films) {
            filmIds.add(film.getId());
        }
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        String sql = "SELECT user_id FROM film_likes WHERE film_id IN (:filmIds)";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("filmIds", filmIds);
        return namedParameterJdbcTemplate.queryForList(sql, parameters, Long.class);
    }

}
