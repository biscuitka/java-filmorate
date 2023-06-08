package ru.yandex.practicum.filmorate.storage.friends;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class InMemoryFriendsStorage implements FriendsStorage {
    private final UserStorage userStorage;

    public void addFriend(long userId, long friendId) {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        if (user != null && friend != null) {
            user.getFriends().add(friendId);
            friend.getFriends().add(userId);
            userStorage.updateUser(user);
            userStorage.updateUser(friend);
        }
    }

    public void deleteFriend(long userId, long friendId) {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        if (user != null && friend != null) {
            user.getFriends().remove(friendId);
            friend.getFriends().remove(userId);
            userStorage.updateUser(user);
            userStorage.updateUser(friend);
        }
    }

    public List<Long> getAllFriends(long userId) {
        User user = userStorage.getUserById(userId);
        return new ArrayList<>(user.getFriends());
    }

    public List<Long> getCommonFriends(long userId, long otherId) {
        User user = userStorage.getUserById(userId);
        User otherUser = userStorage.getUserById(otherId);
        Set<Long> userFriends = new HashSet<>(user.getFriends());
        Set<Long> otherUserFriends = new HashSet<>(otherUser.getFriends());
        userFriends.retainAll(otherUserFriends);

        return new ArrayList<>(userFriends);

    }
}
