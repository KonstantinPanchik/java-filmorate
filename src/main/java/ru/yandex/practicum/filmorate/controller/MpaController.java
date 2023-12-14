package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.servise.MpaServise;

import java.util.List;

@Validated
@Slf4j
@RestController
@RequestMapping("/mpa")
public class MpaController {

    MpaServise mpaServise;

    @Autowired
    MpaController(MpaServise mpaService) {
        this.mpaServise = mpaService;
    }

    @GetMapping
    public List<MPA> getAllGenre() {
        return mpaServise.getAllMpa();
    }

    @GetMapping("/{id}")
    public MPA getGenreById(@PathVariable Integer id) {
        return mpaServise.getMpaById(id);
    }


}

