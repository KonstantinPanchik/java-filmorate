package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.LikeException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;


import java.sql.ResultSet;
import java.sql.SQLException;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Slf4j
@Component
public class FilmDbStorage implements FilmStorage {

    JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film addFilm(Film newFilm) {
        addFilmInDb(newFilm);

        Film resultFilm = getFilmFromDbByName(newFilm.getName());

        long newFilmId = resultFilm.getId();

        addGenresInToDb(newFilmId, newFilm.getGenres());

        resultFilm = setGenresInFilm(resultFilm);

        return resultFilm;
    }

    @Override
    public Film updateFilm(Film film) {

        if (updateFilmInDb(film) == 0) {
            throw new FilmNotFoundException("Фильм не найден");
        }

        long filmId = film.getId();

        addGenresInToDb(filmId, film.getGenres());

        Film resultFilm = getFilmFromDbById(filmId);

        resultFilm = setGenresInFilm(resultFilm);

        return resultFilm;

    }

    @Override
    public Film getFilmById(long id) {
        return setGenresInFilm(getFilmFromDbById(id));
    }


    @Override
    public List<Film> getAllFilms() {
        return getAllFilmsFromDb().stream().map(film -> setGenresInFilm(film)).collect(Collectors.toList());
    }


    @Override
    public List<Film> getTopFilms(int limit) {
        return getTopFilmFromDb(limit).stream().map(film -> setGenresInFilm(film)).collect(Collectors.toList());
    }

    @Override
    public Film putLike(long userId, long filmId) {
        String sql = "MERGE INTO likes (film_id,user_id) VALUES(?,?)";
        int amount = jdbcTemplate.update(sql, filmId, userId);
        return getFilmById(filmId);
    }

    @Override
    public Film deleteLike(long userId, long filmId) {
        String sql = "DELETE FROM likes WHERE (film_id=? AND user_id=?);";
        int amount = jdbcTemplate.update(sql, filmId, userId);
        if (amount == 0) {
            throw new LikeException();
        }
        return getFilmById(filmId);
    }

    private Film setGenresInFilm(Film film) {
        List<Genre> genres = getGenreByIdFromDb(film.getId());
        film.setGenres(genres);
        return film;
    }

    private void addFilmInDb(Film film) {
        String sql = "INSERT INTO film(name,description,release_date,duration,mpa_id)" +
                "VALUES(?,?,?,?,?)";

        int amount = jdbcTemplate.update(sql, film.getName()
                , film.getDescription()
                , film.getReleaseDate().toString()
                , film.getDuration()
                , film.getMpa().getId());

    }

    private int updateFilmInDb(Film film) {
        String sql = "UPDATE film SET NAME = ?, description = ?, release_date = ? ," +
                " duration = ?, mpa_id = ? WHERE film_id = ? ;";

        int result = jdbcTemplate.update(sql, film.getName()
                , film.getDescription()
                , film.getReleaseDate().toString()
                , film.getDuration()
                , film.getMpa().getId()
                , film.getId());
        return result;
    }

    private Film getFilmFromDbByName(String name) {
        String sqlFoGetFilm = "SELECT f.FILM_ID, f.NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, f.MPA_ID, m.NAME AS mpa_name \n" +
                "FROM film AS f \n" +
                "LEFT JOIN MPA AS m ON m.MPA_ID =f.MPA_ID  \n" +
                "WHERE f.NAME = ? ;";
        Film filmFrDb = jdbcTemplate.queryForObject(sqlFoGetFilm, (rs, rowNum) -> makeFilm(rs), name);
        return filmFrDb;
    }

    private Film getFilmFromDbById(long id) {
        String sqlFoGetFilm = "SELECT f.FILM_ID, f.NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, f.MPA_ID, m.NAME AS mpa_name \n" +
                "FROM film AS f \n" +
                "LEFT JOIN MPA AS m ON m.MPA_ID =f.MPA_ID  \n" +
                "WHERE f.FILM_ID = ? ;";
        Film filmFrDb = jdbcTemplate.queryForObject(sqlFoGetFilm, (rs, rowNum) -> makeFilm(rs), id);
        return filmFrDb;
    }

    private List<Film> getAllFilmsFromDb() {
        String sql = "SELECT f.FILM_ID, f.NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, f.MPA_ID, m.NAME AS mpa_name " +
                " FROM film AS f " +
                " LEFT JOIN MPA AS m ON m.MPA_ID =f.MPA_ID;";

        List<Film> result = jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));

        return result;
    }

    private List<Film> getTopFilmFromDb(int limit) {
        String sql = "SELECT f.FILM_ID, f.NAME, f.DESCRIPTION ,f.RELEASE_DATE," +
                " f.DURATION, f.MPA_ID, m.name AS mpa_name, COUNT(l.user_id) AS likes" +
                " FROM film as f " +
                "LEFT JOIN LIKES AS l ON l.FILM_ID=f.FILM_ID " +
                "LEFT JOIN MPA AS m ON m.MPA_ID =f.MPA_ID " +
                "GROUP BY  f.FILM_ID " +
                "ORDER BY COUNT(l.user_id) DESC " +
                "LIMIT ?;";
        List<Film> result = jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), limit);
        return result;
    }

    private Film makeFilm(ResultSet resultSet) throws SQLException {
        Film result = new Film(resultSet.getString("name")
                , LocalDate.parse(resultSet.getString("release_date")));

        result.setId(resultSet.getLong("film_id"));
        result.setDuration(resultSet.getInt("duration"));
        result.setDescription(resultSet.getString("description"));

        MPA mpa = new MPA();
        mpa.setId(resultSet.getInt("mpa_id"));

        mpa.setName(resultSet.getString("mpa_name"));

        result.setMpa(mpa);

        return result;
    }

    private List<Genre> getGenreByIdFromDb(long filmId) {
        String sql = "SELECT g.genre_id, name FROM FILM_GENRE fg " +
                "LEFT JOIN GENRE g ON g.GENRE_ID =fg.GENRE_ID WHERE FILM_ID =?" +
                " ORDER BY g.genre_id ;";
        List<Genre> result = jdbcTemplate.query(sql, (rs, rowNum) -> GenreDbStorage.makeGenre(rs), filmId);
        return result;
    }


    private void addGenresInToDb(Long id, Collection<Genre> genres) {
        if (id == null || genres == null) {
            return;
        }

        Set<Genre> genresSet = genres.stream().collect(Collectors.toSet());

        String sqlQueryforDeletionAllRows = "DELETE FROM FILM_GENRE WHERE film_id=?;";
        jdbcTemplate.update(sqlQueryforDeletionAllRows, id);
        if (genres.size() > 0) {
            for (Genre genre : genresSet) {
                String sqlQuery = "INSERT INTO film_genre (film_id, genre_id) " +
                        "VALUES (?, ?);";
                jdbcTemplate.update(sqlQuery, id, genre.getId());
            }
        }
    }
}
