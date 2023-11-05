package ru.yandex.practicum.filmorate.validationTest;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.*;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Set;

@SpringBootTest
@Validated
public class UserValidationTest {

    UserController userController = new UserController();

    ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    Validator validator = validatorFactory.usingContext().getValidator();

    @Test
    public void shouldCreateUser() {
        User user1 = new User("roma@mail.ru", LocalDate.of(2009, 12, 1));
        user1.setLogin("ololo");
        Set validatored = validator.validate(user1);
        assertTrue(validatored.size() == 0);

    }

    @Test
    public void shouldNotCreateUserWithWrongEmail() {
        User user1 = new User("romaail.ru", LocalDate.of(2009, 12, 1));
        user1.setLogin("ololo");
        Set validatored = validator.validate(user1);
        assertTrue(validatored.size() == 1);

    }

    @Test
    public void shouldNotCreateUserWithWrongDate() {
        User user1 = new User("roma@mail.ru", LocalDate.of(2027, 12, 1));
        user1.setLogin("ololo");
        Set validatored = validator.validate(user1);
        assertTrue(validatored.size() == 1);

    }

    @Test
    public void shouldNotCreateUserWithWrongLogin() {
        User user1 = new User("roma@mail.ru", LocalDate.of(2020, 12, 1));
        user1.setLogin("olo o lo");
        Set validatored = validator.validate(user1);
        assertTrue(validatored.size() == 1);

    }

    @Test
    public void shouldNotCreateUserWithBlankLogin() {
        User user1 = new User("roma@mail.ru", LocalDate.of(2020, 12, 1));
        user1.setLogin("      ");
        Set validatored = validator.validate(user1);
        assertTrue(validatored.size() == 1);
    }
    @Test
    public void shouldThrowExceptionNullLogin() {
        User user1 = new User("roma@mail.ru", LocalDate.of(2020, 12, 1));
        user1.setLogin(null);
        assertThrows(Throwable.class,()->{
            validator.validate(user1);
        });
    }

}
