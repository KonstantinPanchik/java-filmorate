package ru.yandex.practicum.filmorate.validationTest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
@Validated
public class FilmValidationTest {

    FilmController filmController = new FilmController();

    ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    Validator validator = validatorFactory.usingContext().getValidator();

    @Test
    public void shouldValidateFilm() {
        Film film = new Film("Форсаж1", LocalDate.of(2001, 1, 1));
        film.setDescription("Полу покер влюбляется в сестру вина дизиля и они Виииииууу такой звук дают");
        film.setDuration(120);
        Set validatored = validator.validate(film);
        assertTrue(validatored.size() == 0);

    }
    @Test
    public void shouldNotValidateDescription (){
        Film film = new Film("Форсаж1", LocalDate.of(2001, 1, 1));
        film.setDescription("Полу покер влюбляется в сестру вина дизиля и они Виииииииииииииииииииииииииииииииииууу " +
                        "ууииииииииииииииииииииииииииииииииииииииииииииии" +
                "ууууууууууууууу ААААААААА!!!!! (звук колес ) взрыв бабах тутутуту такой звук дают " +
                "СЕМЬЯ ЭТО ГЛАВНОЕ ");
        film.setDuration(120);
        Set validatored = validator.validate(film);
        assertTrue(validatored.size() == 1);

    }
    @Test
    public void shouldNotValidateFilmWithDateBefore1895() {
        Film film = new Film("Крещение руси", LocalDate.of(998, 1, 1));
        film.setDescription("Злой ящер снял на скрытую камеру как русы крестятся");
        film.setDuration(120);
        Set validatored = validator.validate(film);
        assertTrue(validatored.size() == 1);

    }
    @Test
    public void shouldValidateFilmWithDateEquals1895_12_28() {
        Film film = new Film("Прибытие поезда", LocalDate.of(1895, 12, 28));
        film.setDuration(1);
        Set validatored = validator.validate(film);
        assertTrue(validatored.size() == 0);
    }

    @Test
    public void shouldNotValidateNegativDuration() {
        Film film = new Film("Прибытие поезда", LocalDate.of(1895, 12, 28));
        film.setDuration(-7);
        Set validatored = validator.validate(film);
        assertTrue(validatored.size() == 1);
    }
    @Test
    public void shouldNotValidateZeroDuration() {
        Film film = new Film("Прибытие поезда", LocalDate.of(1895, 12, 28));
        film.setDuration(0);
        Set validatored = validator.validate(film);
        assertTrue(validatored.size() == 1);
    }

}
