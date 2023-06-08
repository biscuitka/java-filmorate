package ru.yandex.practicum.filmorate.storage.friends;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

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
    public List<Long> getAllFriends(long userId) {
        String sql = "SELECT friend_id " +
                "FROM friends " +
                "WHERE user_id = ?";
        return jdbcTemplate.queryForList(sql, Long.class, userId);
    }

    @Override
    public List<Long> getCommonFriends(long userId, long otherId) {
        String sql = "SELECT f1.friend_id " +
                "FROM friends f1 JOIN friends f2 ON f1.friend_id = f2.friend_id " +
                "WHERE f1.user_id = ? " +
                "AND f2.user_id = ?";
        return jdbcTemplate.queryForList(sql, Long.class, userId, otherId);
    }
}
