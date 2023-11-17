package ru.yandex.practicum.filmorate.validators;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.Month;

public class LaterThanDateValidator implements ConstraintValidator<LaterThanDate, LocalDate> {
    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        LocalDate startDate = LocalDate.of(1895, Month.DECEMBER, 28);
        return localDate.isAfter(startDate) || localDate.isEqual(startDate);
    }
}