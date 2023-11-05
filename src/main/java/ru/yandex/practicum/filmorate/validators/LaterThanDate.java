package ru.yandex.practicum.filmorate.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LaterThanDateValidator.class)
@Documented
public @interface LaterThanDate {

    String message() default "{Дата раньше чем 1895-12-28}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}


