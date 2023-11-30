package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.servise.FilmService;
import ru.yandex.practicum.filmorate.servise.UserService;

import javax.validation.Valid;

import java.util.List;


@Validated
@Slf4j
@RestController
@RequestMapping("/films")
public class
FilmController {

    FilmService filmService;
    UserService userService;

    @Autowired
    public FilmController(FilmService filmService, UserService userService) {
        this.filmService = filmService;
        this.userService = userService;
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        return filmService.getFilmStorage().addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        return filmService.getFilmStorage().updateFilm(film);
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return filmService.getFilmStorage().getAllFilms();
    }

    @GetMapping("/{id}")
    public Film getFilmByID(@PathVariable Long id) {
        return filmService.getFilmStorage().getFilmById(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film putLike(@PathVariable long id, @PathVariable long userId) {
        Film likedFilm = filmService.getFilmStorage().getFilmById(id);
        filmService.putLike(userService.getUserStorage().getUserById(userId), likedFilm);
        return likedFilm;
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLike(@PathVariable long id, @PathVariable long userId) {
        Film likedFilm = filmService.getFilmStorage().getFilmById(id);
        filmService.removeLike(userService.getUserStorage().getUserById(userId), likedFilm);
        return likedFilm;
    }

    @GetMapping("/popular")
    public List<Film> top(@RequestParam(required = false, defaultValue = "10") String count) {
        Integer limit = Integer.parseInt(count);
        return filmService.topFilms(limit);
    }

}
