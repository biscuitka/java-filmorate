package ru.yandex.practicum.filmorate.storage.friends;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendsStorage {
    void addFriend(long userId, long friendId);

    void deleteFriend(long userId, long friendId);

    List<User> getAllFriends(long userId);

    List<User> getCommonFriends(long userId, long otherId);
}
