package ru.yandex.practicum.filmorate.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class DateOfReleaseValidator implements ConstraintValidator<DateOfRelease, LocalDate> {
    private final LocalDate birthdayOfFilm = LocalDate.of(1895, 12, 28);
    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext context) {
        return date.isAfter(birthdayOfFilm);
    }
}

