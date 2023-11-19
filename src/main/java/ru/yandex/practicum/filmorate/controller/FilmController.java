package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.servise.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.Valid;

import java.util.List;


@Validated
@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    FilmStorage filmStorage;
    FilmService filmService;
    UserStorage userStorage;

    @Autowired
    public FilmController(FilmStorage filmStorage
            , FilmService filmService
            , UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
        this.userStorage = userStorage;
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        return filmStorage.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        return filmStorage.updateFilm(film);
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    @GetMapping("/{id}")
    public Film getFilmByID(@PathVariable Long id) {
        return filmStorage.getFilmById(id);
    }

    @PutMapping("/{id}/like/{userId}")//PUT /films/{id}/like/{userId}
    public Film putLike(@PathVariable long id, @PathVariable long userId) {
        Film likedFilm = filmStorage.getFilmById(id);
        filmService.putLike(userStorage.getUserById(userId), likedFilm);
        return likedFilm;
    }

    @DeleteMapping("/{id}/like/{userId}")//DELETE /films/{id}/like/{userId}
    public Film deleteLike(@PathVariable long id, @PathVariable long userId) {
        Film likedFilm = filmStorage.getFilmById(id);
        filmService.removeLike(userStorage.getUserById(userId), likedFilm);
        return likedFilm;
    }

    @GetMapping("/popular")//GET /films/popular?count={count}
    public List<Film> top(@RequestParam(required = false, defaultValue = "10") String count) {
        Integer limit = Integer.parseInt(count);
        return filmService.top10films(filmStorage, limit);
    }

}
