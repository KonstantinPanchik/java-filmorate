package ru.yandex.practicum.filmorate.storage;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class GenreDbStorage {


    JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public static Genre makeGenre(ResultSet rs) throws SQLException {
        Genre genre = new Genre();
        genre.setId(rs.getInt("genre_id"));
        genre.setName(rs.getString("name"));
        return genre;
    }

    public List<Genre> getAllGenreFromDb() {
        String sql = "SELECT * FROM genre ORDER BY genre_id";
        return jdbcTemplate.query(sql, (rs, rownum) -> makeGenre(rs));
    }

    public Genre getGenreFromDb(int id) {
        String sql = "SELECT * FROM genre WHERE genre_id=?";
        try {
            Genre genre = jdbcTemplate.queryForObject(sql, (rs, rownum) -> makeGenre(rs), id);
            return genre;
        }catch (EmptyResultDataAccessException e){
            throw new GenreNotFoundException("Жанр с id = "+id+" не найден!");
        }
    }

}
