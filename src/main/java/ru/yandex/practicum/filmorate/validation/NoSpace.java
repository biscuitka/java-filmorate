package ru.yandex.practicum.filmorate.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Constraint(validatedBy = NoSpaseValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface NoSpace {
    String message() default "Логин не должен содержать пробелов";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}