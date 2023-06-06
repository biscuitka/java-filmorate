package ru.yandex.practicum.filmorate.storage.genres;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class GenresDbStorage implements GenresStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Genre getById(int id) {
        try {
            return jdbcTemplate.queryForObject("SELECT genre_id, name FROM genres WHERE genre_id = ?",
                    new GenreMapper(), id);
        } catch (EmptyResultDataAccessException exp) {
            log.error("Ошибка при запросе жанра по id {} " + exp.getMessage(), id);
            throw new NotFoundException("Жанр с id " + id + " не найден");
        }
    }

    @Override
    public List<Genre> getAll() {
        return jdbcTemplate.query("SELECT genre_id, name FROM genres", new GenreMapper());
    }

    @Override
    public List<Integer> getGenresByFilm(long filmId) {
        String sql = "SELECT genre_id FROM film_genre WHERE film_id = ?";
        return jdbcTemplate.queryForList(sql, Integer.class, filmId);
    }
}
