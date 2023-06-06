package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Mpa getById(int id) {
        try {
            return jdbcTemplate.queryForObject("SELECT mpa_id, name FROM mpa WHERE mpa_id = ?",
                    new MpaMapper(), id);
        } catch (EmptyResultDataAccessException exp) {
            log.error("Ошибка при запросе рейтинга по id {} " + exp.getMessage(), id);
            throw new NotFoundException("Возрастной рейтинг с id " + id + " не найден");
        }

    }

    @Override
    public List<Mpa> getAll() {
        return jdbcTemplate.query("SELECT mpa_id, name FROM mpa", new MpaMapper());
    }

    @Override
    public Mpa getByFilmId(long filmId) {
        String sql = "SELECT f.mpa_id, m.name FROM films AS f LEFT JOIN mpa AS m " +
                "ON f.mpa_id = m.mpa_id WHERE f.film_id = ?";
        return jdbcTemplate.queryForObject(sql, new MpaMapper(), filmId);
    }
}
