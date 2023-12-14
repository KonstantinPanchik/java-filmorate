package ru.yandex.practicum.filmorate.storage;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.MPA;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class MpaDbStorage {


    JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    private MPA makeMpa(ResultSet rs) throws SQLException {
        MPA mpa = new MPA();
        mpa.setId(rs.getInt("mpa_id"));
        mpa.setName(rs.getString("name"));
        return mpa;
    }

    public List<MPA> getAllMpaFromDb() {
        String sql = "SELECT * FROM mpa ORDER BY mpa_id;";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeMpa(rs));
    }

    public MPA getMpaFromDb(int id) {
        String sql = "SELECT * FROM mpa WHERE mpa_id = ?;";
        try {
            MPA mpa = jdbcTemplate.queryForObject(sql, (rs, rownum) -> makeMpa(rs), id);
            return mpa;
        }catch (EmptyResultDataAccessException e){
            throw new MpaNotFoundException("Жанр с id = "+id+" не найден!");
        }
    }

}


