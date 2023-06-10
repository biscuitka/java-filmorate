package ru.yandex.practicum.filmorate.storage.friends;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserMapper;

import java.util.List;

@Component
@RequiredArgsConstructor
@Primary
public class FriendsDbStorage implements FriendsStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addFriend(long userId, long friendId) {
        String sql = "INSERT INTO friends (user_id, friend_id) " +
                "VALUES (?, ?)";
        jdbcTemplate.update(sql, userId, friendId);
    }

    @Override
    public void deleteFriend(long userId, long friendId) {
        String sql = "DELETE FROM friends " +
                "WHERE user_id =? AND friend_id = ?";
        jdbcTemplate.update(sql, userId, friendId);
    }

    @Override
    public List<User> getAllFriends(long userId) {
        String sql = "SELECT u.user_id, " +
                "u.name, " +
                "u.login, " +
                "u.email, " +
                "u.birthday " +
                "FROM users AS u " +
                "LEFT JOIN friends AS f ON f.friend_id = u.user_id " +
                "WHERE f.user_id = ?";
        return jdbcTemplate.query(sql, new UserMapper(), userId);
    }

    @Override
    public List<User> getCommonFriends(long userId, long otherId) {
        String sql = "SELECT u.user_id, " +
                "u.name, " +
                "u.login, " +
                "u.email, " +
                "u.birthday " +
                "FROM users AS u " +
                "JOIN friends AS f1 ON f1.friend_id = u.user_id " +
                "JOIN friends AS f2 ON f2.friend_id = u.user_id " +
                "WHERE f1.user_id = ? " +
                "AND f2.user_id = ?";

        return jdbcTemplate.query(sql, new UserMapper(), userId, otherId);
    }
}
