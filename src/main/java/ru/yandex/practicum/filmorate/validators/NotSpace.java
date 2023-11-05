package ru.yandex.practicum.filmorate.validators;



import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotSpaceValidator.class)
@Documented
public @interface NotSpace {
    String message() default "В логине не может быть пробелов";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
