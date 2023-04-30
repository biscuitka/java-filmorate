package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.validation.NoSpace;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class User {
    private int id;

    @NotNull(message = "Поле email обязательно для заполнения")
    @Email(regexp = "^([a-zA-Z0-9._%+-]+)@([a-zA-Z0-9.-]+.[a-zA-Z]{2,})$", message = "Введен некорректный email")
    private String email;

    @NotNull(message = "Поле login обязательно для заполнения")
    @NotBlank(message = "Поле login не может быть пустым")
    @NoSpace
    private String login;

    private String name;

    @Past(message = "Дата рождения не может быть в будущем")
    @NotNull(message = "Поле birthday обязательно для заполнения")
    private LocalDate birthday;
}
