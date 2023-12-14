package ru.yandex.practicum.filmorate.DBtest;

import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import ru.yandex.practicum.filmorate.model.Film;

import ru.yandex.practicum.filmorate.model.Genre;

import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;


import java.time.LocalDate;
import java.util.HashSet;

import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@JdbcTest // указываем, о необходимости подготовить бины для работы с БД
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTest {

    private final JdbcTemplate jdbcTemplate;



    @BeforeEach
    public  void prepare() {

        jdbcTemplate.update("MERGE INTO film(film_id,name,description,duration,release_date,mpa_id)\n" +
                "VALUES(2,'Ёлки','Новый год чудесный',90,'2020-12-31',1),\n" +
                "(3,'Форсаж','Гонки и лысый',120,'2007-08-16',3),\n" +
                "(4,'Форсаж 2','Пол покер и черный',108,'2009-08-16',3);");
        jdbcTemplate.update("MERGE INTO film_genre(film_id,genre_id)\n" +
                "VALUES(2,1),(2,2),(2,3),\n" +
                "(3,2),(3,4),\n" +
                "(4,1),(4,4);");
    }


    @Test
    public void testAddFilm() {
        Film film1 = new Film("ФОРСАЖ 4", LocalDate.of(2018, 10, 4));
        film1.setDescription("Гонки что-то происходило и лысый вернулся");
        film1.setDuration(985);
        film1.setId(1);

        MPA mpa1 = new MPA();
        mpa1.setId(1);
        film1.setMpa(mpa1);

        Set<Genre> genres1 = new HashSet<>();
        for (int i = 1; i < 6; i++) {
            Genre genre = new Genre();
            genre.setId(i);
            genres1.add(genre);
        }
        film1.setGenres(genres1);

        FilmDbStorage filmDbStorage = new FilmDbStorage(jdbcTemplate);
        // вызываем тестируемый метод
        Film savedFilm = filmDbStorage.addFilm(film1);
        // проверяем утверждения
        assertThat(savedFilm).isNotNull();// проверяем, что объект не равен null

        assertEquals(film1.getId(), savedFilm.getId()); //id
        assertEquals(film1.getName(), savedFilm.getName()); //name
        assertEquals(film1.getDescription(), savedFilm.getDescription()); //description
        assertEquals(film1.getMpa().getId(), savedFilm.getMpa().getId()); //mpaId
        assertEquals(film1.getReleaseDate(), savedFilm.getReleaseDate()); //releaseDate
        assertEquals(film1.getDuration(), savedFilm.getDuration()); //duration

        Set<Integer> newFilmGenres = film1.getGenres().stream().map((genre -> genre.getId())).collect(Collectors.toSet());
        Set<Integer> savedFilmGenres = savedFilm.getGenres().stream().map((genre -> genre.getId())).collect(Collectors.toSet());

        assertEquals(newFilmGenres, savedFilmGenres); //genresId
    }

    @Test
    public void testUpdateFilm() {
        Film newFilm = new Film("Ёлки 2", LocalDate.of(2010, 1, 1));
        newFilm.setDescription("Светлаков и ургант опять в деле");
        newFilm.setDuration(100);
        newFilm.setId(2);

        MPA mpa1 = new MPA();
        mpa1.setId(1);
        newFilm.setMpa(mpa1);

        Set<Genre> genres1 = new HashSet<>();
        for (int i = 1; i < 6; i++) {
            Genre genre = new Genre();
            genre.setId(i);
            genres1.add(genre);
        }
        newFilm.setGenres(genres1);

        FilmDbStorage filmDbStorage = new FilmDbStorage(jdbcTemplate);

        Film updatedFilm = filmDbStorage.updateFilm(newFilm);

        // проверяем утверждения
        assertThat(updatedFilm).isNotNull();// проверяем, что объект не равен null

        assertEquals(newFilm.getId(), updatedFilm.getId()); //id
        assertEquals(newFilm.getName(), updatedFilm.getName()); //name
        assertEquals(newFilm.getDescription(), updatedFilm.getDescription()); //description
        assertEquals(newFilm.getMpa().getId(), updatedFilm.getMpa().getId()); //mpaId
        assertEquals(newFilm.getReleaseDate(), updatedFilm.getReleaseDate()); //releaseDate
        assertEquals(newFilm.getDuration(), updatedFilm.getDuration()); //duration

        Set<Integer> newFilmGenres = newFilm.getGenres().stream().map((genre -> genre.getId())).collect(Collectors.toSet());
        Set<Integer> savedFilmGenres = updatedFilm.getGenres().stream().map((genre -> genre.getId())).collect(Collectors.toSet());

        assertEquals(newFilmGenres, savedFilmGenres); //genresId
    }

    @Test
    public void testGetFilmById() {


        FilmDbStorage filmDbStorage = new FilmDbStorage(jdbcTemplate);

        Film gotFilmWith2id = filmDbStorage.getFilmById(2);


        // проверяем утверждения
        assertThat(gotFilmWith2id).isNotNull();// проверяем, что объект не равен null

        assertEquals(2, gotFilmWith2id.getId()); //id
        assertEquals("Ёлки", gotFilmWith2id.getName()); //name
        assertEquals("Новый год чудесный", gotFilmWith2id.getDescription()); //description
        assertEquals(90, gotFilmWith2id.getDuration()); //duration

    }

    @Test
    public void testAllFilm() {


        FilmDbStorage filmDbStorage2 = new FilmDbStorage(jdbcTemplate);

        assertEquals(3, filmDbStorage2.getAllFilms().size());
        assertEquals(2, filmDbStorage2.getTopFilms(2).size());
    }

}


