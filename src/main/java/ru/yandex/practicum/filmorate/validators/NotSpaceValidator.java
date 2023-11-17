package ru.yandex.practicum.filmorate.validators;


import lombok.NonNull;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotSpaceValidator implements ConstraintValidator<NotSpace, String> {

    @Override
    public boolean isValid(@NonNull String s, ConstraintValidatorContext constraintValidatorContext) {

        char[] chars = s.toCharArray();
        char space = ' ';
        for (char a : chars) {
            if (a == space) {
                return false;
            }
        }
        return true;

    }
}
