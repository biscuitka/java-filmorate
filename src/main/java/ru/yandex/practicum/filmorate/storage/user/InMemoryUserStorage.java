package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ExistException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();
    private int idGenerator = 1;

    @Override
    public User createUser(User user) {
        if ((users.values()).stream().noneMatch(user1 -> user1.getLogin().equals(user.getLogin())) &&
                (users.values()).stream().noneMatch(user1 -> user1.getEmail().equals(user.getEmail()))) {
            user.setId(idGenerator++);
            if (user.getName() == null || user.getName().equals("")) {
                user.setName(user.getLogin());
            }
            users.put(user.getId(), user);
        } else {
            throw new ExistException("Пользователь с таким логином/почтой уже существует");
        }
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (user.getId() != 0 && users.containsKey(user.getId())) {
            users.put(user.getId(), user);
        } else {
            throw new NotFoundException("Пользователь не найден");
        }
        return user;
    }

    @Override
    public User deleteUserById(long id) {
        if (id != 0 && users.containsKey(id)) {
            users.remove(id);
        } else {
            throw new NotFoundException("Пользователь не найден");
        }
        return users.get(id);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(long id) {
        if (id != 0 && users.containsKey(id)) {
            return users.get(id);
        } else {
            throw new NotFoundException("Пользователь не найден");
        }
    }
}
