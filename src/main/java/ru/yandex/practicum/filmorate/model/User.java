package ru.yandex.practicum.filmorate.model;


import lombok.Data;
import lombok.NonNull;
import ru.yandex.practicum.filmorate.validators.NotSpace;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Data
public class User {

    int id;
    @NonNull @Email(message = "Неверно задан имэйл")
    String email;
    @NotNull
    @NotSpace(message = "Логин не может содержать пробелы") //проверяет что в переданном стринге нет пробелов
    String login;

    String name;
    @NonNull
    @PastOrPresent(message = "Дата рождения не может быть после текущего числа")
    LocalDate birthday;


}
