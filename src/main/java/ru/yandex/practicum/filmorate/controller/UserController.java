package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.servise.UserService;

import javax.validation.Valid;

import java.util.List;


@Validated
@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {


    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        return userService.getUserStorage().addUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        return userService.getUserStorage().updateUser(user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getUserStorage().getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        User result = userService.getUserStorage().getUserById(id);
        return result;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        User user = userService.getUserStorage().getUserById(id);
        User friend = userService.getUserStorage().getUserById(friendId);
        userService.addFriend(user, friend);
        return user;
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable Long id, @PathVariable Long friendId) {
        User user = userService.getUserStorage().getUserById(id);
        User friend = userService.getUserStorage().getUserById(friendId);
        userService.removeFriend(user, friend);
        return user;
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriend(@PathVariable Long id) {
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        User user = userService.getUserStorage().getUserById(id);
        User friend = userService.getUserStorage().getUserById(otherId);
        return userService.getMutualFriends(user, friend);
    }


}
