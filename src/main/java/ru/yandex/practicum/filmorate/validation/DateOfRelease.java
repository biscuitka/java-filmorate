package ru.yandex.practicum.filmorate.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DateOfReleaseValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DateOfRelease {

    String message() default "Дата релиза должна быть позднее 28.12.1985";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}