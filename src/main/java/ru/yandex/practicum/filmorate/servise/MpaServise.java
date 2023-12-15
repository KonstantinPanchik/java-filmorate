package ru.yandex.practicum.filmorate.servise;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.MpaDbStorage;

import java.util.List;


@Component
public class MpaServise {


    MpaDbStorage mpaDbStorage;

    @Autowired
    MpaServise(MpaDbStorage mpaDbStorage) {
        this.mpaDbStorage = mpaDbStorage;
    }

    public List<MPA> getAllMpa() {
        return mpaDbStorage.getAllMpaFromDb();
    }

    public MPA getMpaById(int id) {
        return mpaDbStorage.getMpaFromDb(id);
    }


}
