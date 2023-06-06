package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
@Primary
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public User createUser(User user) {
        if (user.getName() == null || user.getName().equals("")) {
            user.setName(user.getLogin());
        }
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users").usingGeneratedKeyColumns("user_id");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", user.getName());
        parameters.put("login", user.getLogin());
        parameters.put("email", user.getEmail());
        parameters.put("birthday", user.getBirthday());
        user.setId(insert.executeAndReturnKey(parameters).longValue());
        return user;
    }

    @Override
    public User updateUser(User user) {
        String sqlSelect = "SELECT user_id " +
                "FROM users WHERE user_id = ?";
        try {
            jdbcTemplate.queryForObject(sqlSelect, Long.class, user.getId());
        } catch (EmptyResultDataAccessException exp) {
            log.error("Ошибка при обновлении по id {}" + exp.getMessage(), user.getId());
            throw new NotFoundException("Пользователь с id" + user.getId() + "не найден");
        }
        String sqlUpdate = "UPDATE users SET name = ?, login = ?, email =?, birthday = ? " +
                "WHERE user_id = ?";
        jdbcTemplate.update(sqlUpdate, user.getName(), user.getLogin(),
                user.getEmail(), user.getBirthday(), user.getId());
        return user;
    }

    @Override
    public void deleteUserById(long id) {
        jdbcTemplate.update("DELETE FROM users WHERE user_id = ?", id);
    }

    @Override
    public List<User> getAllUsers() {
        String sql = "SELECT user_id, name, login, email, birthday " +
                "FROM users";
        return jdbcTemplate.query(sql, new UserMapper());
    }

    @Override
    public User getUserById(long id) {
        String sql = "SELECT * " +
                "FROM users WHERE user_id = ?";
        // return jdbcTemplate.queryForObject(sql, new UserMapper(), id);
        try {
            return jdbcTemplate.queryForObject(sql, new UserMapper(), id);
        } catch (EmptyResultDataAccessException exp) {
            log.error("Ошибка при запросе по id: {} ." + exp.getMessage(), id);
            throw new NotFoundException("Пользователь с id" + id + "не найден");
        }

    }
}
