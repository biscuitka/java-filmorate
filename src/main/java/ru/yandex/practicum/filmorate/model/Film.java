package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.validation.DateOfRelease;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
public class Film {
    private long id;

    @NotNull(message = "Поле name обязательно для заполнения")
    @NotBlank(message = "Поле name не может быть пустым")
    private String name;

    @Size(max = 200, message = "Описание не должно превышать 200 символов")
    private String description;

    @DateOfRelease
    private LocalDate releaseDate;

    @Positive(message = "Продолжительность не может быть отрицательной")
    private Long duration;

    private Set<Long> likes = new HashSet<>();
    private LinkedHashSet<Genre> genres = new LinkedHashSet<>();
    private Mpa mpa;

    public boolean containsLike(Long userId) {
        return likes.contains(userId);
    }
}
