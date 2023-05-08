package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserStorage userStorage;
    private final UserService userService;

    public UserController(UserStorage userStorage, UserService userService) {
        this.userStorage = userStorage;
        this.userService = userService;
    }


    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Создание пользователя: {}", user);
        return userStorage.createUser(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("Обновление пользователя: {}", user);
        return userStorage.updateUser(user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable int id) {
        return userStorage.getUserById(id);
    }

    @DeleteMapping("/{id}")
    public User deleteById(@PathVariable int id) {
        return userStorage.deleteUserById(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable long id, @PathVariable long friendId) {
        log.info("Добавление {} в друзья к {}", friendId, id);
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable long id, @PathVariable long friendId) {
        log.info("Удаление {} из друзей {}", friendId, id);
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getAllFriends(@PathVariable long id) {
        return userService.getAllFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable long id, @PathVariable long otherId) {
        return userService.getCommonFriends(id, otherId);
    }
}
