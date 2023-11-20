package ru.yandex.practicum.filmorate.servise;

import lombok.Data;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Data
public class FilmService {

    FilmStorage filmStorage;
    @Autowired
    public FilmService(FilmStorage filmStorage){
        this.filmStorage=filmStorage;
    }

    public boolean putLike(@NonNull User user, @NonNull Film film) {
        return film.getLikes().add(user.getId());
    }

    public boolean removeLike(@NonNull User user, @NonNull Film film) {
        return film.getLikes().remove(user.getId());
    }

    public List<Film> topFilms(int limit) {
        List<Film> result = filmStorage.getAllFilms().stream()
                .sorted(((f1, f2) -> f2.getLikes().size() - f1.getLikes().size()))// .sorted(((f1, f2) -> f1.getLikes().size() - f2.getLikes().size()))
                .limit(limit).collect(Collectors.toList());
        return result;

    }

}
