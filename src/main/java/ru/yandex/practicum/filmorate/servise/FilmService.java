package ru.yandex.practicum.filmorate.servise;

import lombok.NonNull;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {

    public boolean putLike(@NonNull User user, @NonNull Film film) {
        return film.getLikes().add(user.getId());
    }

    public boolean removeLike(@NonNull User user, @NonNull Film film) {
        return film.getLikes().remove(user.getId());
    }

    public List<Film> top10films(FilmStorage filmStorage, int limit) {
        List<Film> result = filmStorage.getAllFilms().stream()
                .sorted(((f1, f2) -> f2.getLikes().size() - f1.getLikes().size()))// .sorted(((f1, f2) -> f1.getLikes().size() - f2.getLikes().size()))
                .limit(limit).collect(Collectors.toList());
        return result;

    }

}
