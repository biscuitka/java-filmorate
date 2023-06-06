package ru.yandex.practicum.filmorate.storage.friends;

import java.util.List;

public interface FriendsStorage {
    void addFriend(long userId, long friendId);

    void deleteFriend(long userId, long friendId);

    List<Long> getAllFriends(long userId);

    List<Long> getCommonFriends(long userId, long otherId);
}
