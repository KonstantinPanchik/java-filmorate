package ru.yandex.practicum.filmorate.servise;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreDbStorage;

import java.util.List;


@Component
public class GenreService {


    GenreDbStorage genreDbStorage;

    @Autowired
    GenreService(GenreDbStorage genreDbStorage){
        this.genreDbStorage=genreDbStorage;
    }

    public List<Genre> getAllGenres(){
        return genreDbStorage.getAllGenreFromDb();
    }

    public Genre getGenreById(int id){
        return genreDbStorage.getGenreFromDb(id);
    }



}
