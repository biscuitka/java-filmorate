package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
public class UserValidationTest {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    public void testEmptyData(){
        User user = new User();
        var violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertEquals(5, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Поле birthday обязательно для заполнения")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Поле email обязательно для заполнения")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Поле login обязательно для заполнения")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Логин не должен содержать пробелов")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Поле login не может быть пустым")));

    }

    @Test
    public void testIncorrectData() {
        User user = new User();
        user.setEmail("invalid.email");
        user.setLogin("lo gin");
        user.setBirthday(LocalDate.now().plusDays(1)); // будущая дата

        var violations = validator.validate(user);

        assertFalse(violations.isEmpty());
        assertEquals(3, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Введен некорректный email")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Дата рождения не может быть в будущем")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Логин не должен содержать пробелов")));

    }
}
