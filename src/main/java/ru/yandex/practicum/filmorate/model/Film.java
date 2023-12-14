package ru.yandex.practicum.filmorate.model;


import lombok.Data;
import lombok.NonNull;
import ru.yandex.practicum.filmorate.validators.LaterThanDate;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


@Data
public class Film {

    long id;
    @NotBlank
    @NonNull
    String name;
    @Size(max = 200)
    String description;
    @NonNull
    @LaterThanDate //проверяет, что дата позже или равна 1895-12-28
    LocalDate releaseDate;
    @Positive
    int duration;

    MPA mpa;

    List<Genre> genres= new ArrayList<>();


}
