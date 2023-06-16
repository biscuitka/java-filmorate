package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friends.FriendsStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;
    private final FriendsStorage friendsStorage;

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public void deleteUserById(long id) {
        userStorage.deleteUserById(id);
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User getUserById(long id) {
        return userStorage.getUserById(id);
    }

    public void addFriend(long userId, long friendId) {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        if (user != null && friend != null) {
            friendsStorage.addFriend(userId, friendId);
        } else {
            log.info("Не найден пользователь с id {} либо {} ", userId, friendId);
            throw new NotFoundException("Не найден пользователь с id " + userId + " либо " + friendId);
        }
    }

    public void deleteFriend(long userId, long friendId) {
        friendsStorage.deleteFriend(userId, friendId);
    }

    public List<User> getAllFriends(long userId) {
        return friendsStorage.getAllFriends(userId);
    }

    public List<User> getCommonFriends(long userId, long otherId) {
        return friendsStorage.getCommonFriends(userId, otherId);
    }
}
