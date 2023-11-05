package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Validated
@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
   static int createdId;

    private static Map<Integer, User> users = new HashMap<>();

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        user.setId(++createdId);
        log.info("Поступил Пост запрос на создание пользователя");
        if(users.containsKey(user.getId())){
            log.warn("Данный пользователь уже существует");
            throw new ValidationException("Невозможно добавить уже существующего пользователя");
        }
        if(user.getName()==null||user.getName().isBlank()){
            user.setName(user.getLogin());
        }

        log.info("Юзер успешно добавлен");
        users.put(user.getId(),user);
        return user;
    }
    @PutMapping
    public  User updateUser(@Valid @RequestBody User user) {
        if (!(users.containsKey(user.getId()))){
            log.warn("Невозможно обновить неизвестный юзер");
            throw new ValidationException("Невозможно обновить неизвестный юзер");
        }
        users.put(user.getId(),user);
        return user;
    }
    @GetMapping
    public  List<User> getAllUsers(){
        return new ArrayList<>(users.values());
    }



}
