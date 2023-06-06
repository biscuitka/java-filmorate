package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friends.FriendsStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
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
       /* User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        if (user != null && friend != null) {
            user.getFriends().add(friendId);
            friend.getFriends().add(userId);
            userStorage.updateUser(user);
            userStorage.updateUser(friend);
        }

        */
    }

    public void deleteFriend(long userId, long friendId) {
        friendsStorage.deleteFriend(userId, friendId);
        /*User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        if (user != null && friend != null) {
            user.getFriends().remove(friendId);
            friend.getFriends().remove(userId);
            userStorage.updateUser(user);
            userStorage.updateUser(friend);
        }

         */
    }

    public List<User> getAllFriends(long userId) {
        List<User> friends = new ArrayList<>();
        for (Long friendId : friendsStorage.getAllFriends(userId)) {
            User friend = userStorage.getUserById(friendId);
            friends.add(friend);
        }
        return friends;
        /*User user = userStorage.getUserById(userId);
        List<User> friends = new ArrayList<>();
        for (Long friendId : user.getFriends()) {
            User friend = userStorage.getUserById(friendId);
            friends.add(friend);
        }
        return friends;

         */
    }

    public List<User> getCommonFriends(long userId, long otherId) {
        List<User> commonFriends = new ArrayList<>();
        for (Long friendId : friendsStorage.getCommonFriends(userId, otherId)) {
            commonFriends.add(userStorage.getUserById(friendId));
        }

        return commonFriends;
        //return friendsStorage.getCommonFriends(userId, otherId);
        /*User user = userStorage.getUserById(userId);
        User otherUser = userStorage.getUserById(otherId);
        Set<Long> userFriends = new HashSet<>(user.getFriends());
        Set<Long> otherUserFriends = new HashSet<>(otherUser.getFriends());

        userFriends.retainAll(otherUserFriends);

        List<User> commonFriends = new ArrayList<>();
        for (Long friendId : userFriends) {
            commonFriends.add(userStorage.getUserById(friendId));
        }

        return commonFriends;

         */

    }
}
