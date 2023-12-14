package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    Film addFilm(Film film);

    Film updateFilm(Film film);

    List<Film> getAllFilms();

    Film getFilmById(long id);

    List<Film> getTopFilms(int limit);

    public Film putLike(long userId,long filmId);

    public Film deleteLike(long userId,long filmId);

}
