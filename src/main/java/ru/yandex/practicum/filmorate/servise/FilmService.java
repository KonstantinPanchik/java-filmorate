package ru.yandex.practicum.filmorate.servise;

import lombok.Data;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;


@Service
@Data
public class FilmService {
    @Autowired
    @Qualifier("filmDbStorage")
    FilmStorage filmStorage;

    public Film putLike(@NonNull long userId, @NonNull long filmId) {
        return filmStorage.putLike(userId,filmId);
    }

    public Film removeLike(@NonNull long userId, @NonNull long filmId) {
        return filmStorage.deleteLike(userId,filmId);
    }

    public List<Film> topFilms(int limit) {
        List<Film> result = filmStorage.getTopFilms(limit);
        return result;

    }

    public Film getFilmById(long id){
        return filmStorage.getFilmById(id);
    }

}
