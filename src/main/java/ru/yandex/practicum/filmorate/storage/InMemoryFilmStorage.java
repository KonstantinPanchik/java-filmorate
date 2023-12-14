package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    static int createdId;

    private static Map<Long, Film> films = new HashMap<>();

    private static int generateId() {
        return ++createdId;
    }

    @Override
    public Film getFilmById(long id) {
        Film film = films.get(id);
        if (film == null) {
            throw new FilmNotFoundException("Фильм с id = " + id + " не существует");
        }
        return film;
    }

    @Override
    public Film addFilm(Film film) {
        log.info("Поступил Роst запрос /film");
        film.setId(generateId());
        if (films.containsKey(film.getId())) {
            log.warn("Данный Фильм уже существует");
            throw new ValidationException("Невозможно добавить уже существующий фильм");
        }


        films.put(film.getId(), film);
        log.info(film.getId() + " успешно добавлен");
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        log.info("Поступил Рut запрос /film");
        if (!(films.containsKey(film.getId()))) {
            log.warn("Невозможно обновить неизвестный film");
            throw new ValidationException("Невозможно обновить неизвестный film");
        }
        films.put(film.getId(), film);
        log.info(film.getId() + " успешно обновлен");
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public List<Film> getTopFilms(int limit) {
//      return getAllFilms().stream()
//                .sorted(((f1, f2) -> f2.getLikes().size() - f1.getLikes().size()))// .sorted(((f1, f2) -> f1.getLikes().size() - f2.getLikes().size()))
//                .limit(limit).collect(Collectors.toList());
        return null;
    }


    @Override
    public Film putLike(long userId, long filmId) {
        return null;
    }

    @Override
    public Film deleteLike(long userId, long filmId) {
        return null;
    }
}
