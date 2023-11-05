package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Validated
@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    static int createdId;

    private static Map<Integer, Film> films = new HashMap<>();

    public static int generateID() {
        return ++createdId;
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        log.info("Поступил Роst запрос /film");
        film.setId(generateID());
        if (films.containsKey(film.getId())) {
            log.warn("Данный Фильм уже существует");
            throw new ValidationException("Невозможно добавить уже существующий фильм");
        }


        films.put(film.getId(), film);
        log.info(film.getId() + " успешно добавлен");
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Поступил Рut запрос /film");
        if (!(films.containsKey(film.getId()))) {
            log.warn("Невозможно обновить неизвестный film");
            throw new ValidationException("Невозможно обновить неизвестный film");
        }
        films.put(film.getId(), film);
        log.info(film.getId() + " успешно обновлен");
        return film;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }


}
