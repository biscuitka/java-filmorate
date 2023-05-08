package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class FilmValidationTest {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    public void testEmptyData() {
        Film film = new Film();
        film.setReleaseDate(LocalDate.now().minusYears(50));
        var violations = validator.validate(film);
        assertFalse(violations.isEmpty());
        assertEquals(2, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Поле name обязательно для заполнения")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Поле name не может быть пустым")));

    }

    @Test
    public void testIncorrectData() {
        Film film = new Film();
        film.setReleaseDate(LocalDate.now().minusYears(500));
        film.setName("name");
        film.setDescription("Слышу голос из Прекрасного Далека, голос утренний в серебряной росе. " +
                "Слышу голос и манящая дорога кружит голову как в детстве карусель. " +
                "Прекрасное Далеко, не будь ко мне жестоко, не будь ко мне жестоко, жестоко не будь. " +
                "От чистого истока в Прекрасное Далеко, в Прекрасное Далеко я начинаю путь. ");
        film.setDuration(-100L);

        var violations = validator.validate(film);

        assertFalse(violations.isEmpty());
        assertEquals(3, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals(
                "Описание не должно превышать 200 символов")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals(
                "Продолжительность не может быть отрицательной")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals(
                "Дата релиза должна быть позднее 28.12.1985")));

    }
}
