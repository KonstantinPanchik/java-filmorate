package ru.yandex.practicum.filmorate.storage;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {


    static int createdId;

    private static Map<Long, User> users = new HashMap<>();

    private static int generateID() {
        return ++createdId;
    }

    @Override
    public User addUser(User user) {
        user.setId(generateID());
        log.info("Поступил Пост запрос на создание пользователя");
        if (users.containsKey(user.getId())) {
            log.warn("Данный пользователь уже существует");
            throw new ValidationException("Невозможно добавить уже существующего пользователя");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        log.info("Юзер успешно добавлен");
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (!(users.containsKey(user.getId()))) {
            log.warn("Невозможно обновить неизвестный юзер");
            throw new ValidationException("Невозможно обновить неизвестный юзер");
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }


    @Override
    public User getUserById(long id) {
        User user = users.get(id);
        if (user == null) {
            throw new UserNotFoundException("Пользователь с id = " + id + " не найден");

        }
        return user;
    }
}
