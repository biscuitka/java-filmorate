package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ExistException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();
    private int idGenerator = 1;


    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Создание пользователя: {}", user);
        if ((users.values()).stream().noneMatch(user1 -> user1.getLogin().equals(user.getLogin())) &&
                (users.values()).stream().noneMatch(user1 -> user1.getEmail().equals(user.getEmail()))) {
            user.setId(idGenerator++);
            if (user.getName() == null) {
                user.setName(user.getLogin());
            }
            users.put(user.getId(), user);
        } else {
            log.info("Логин {} или почта {} уже используются", user.getLogin(), user.getEmail());
            throw new ExistException("Пользователь с таким логином/почтой уже существует");
        }
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user)  {
        log.info("Обновление пользователя: {}", user);
        if (user.getId() != 0 && users.containsKey(user.getId())) {
            users.put(user.getId(), user);
        } else {
            log.info("Попытка обновления несуществующего пользователя: {}", user);
            throw new NotFoundException("Пользователь не найден");
        }
        return user;
    }

    @GetMapping
    public ArrayList<User> getAllUsers() {
        log.info("Текущее количество пользователей: {}", users.size());
        return new ArrayList<>(users.values());
    }
}
