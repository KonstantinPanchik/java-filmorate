package ru.yandex.practicum.filmorate.servise;


import lombok.Data;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.SameIdException;
import ru.yandex.practicum.filmorate.model.FriendStatus;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.constraints.NotNull;

import java.util.List;


@Service
@Data
public class UserService {

    @Autowired
    @Qualifier("userDbStorage")
    UserStorage userStorage;

    public User addUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return userStorage.addUser(user);
    }


    public FriendStatus addFriend(@NonNull long userId, @NonNull long friendId) {
        if (userId == friendId) {
            throw new SameIdException("Вы не можете добавить в друзья сами себя!!");
        }
        return userStorage.addFriend(userId, friendId);
    }

    public boolean removeFriend(@NonNull long userId, @NonNull long friendId) {

        return userStorage.deleteFriend(userId, friendId);
    }

    public List<User> getMutualFriends(@NonNull Long userId, @NotNull Long friendId) {
        return userStorage.getMutualFriends(userId, friendId);
    }

    public List<User> getFriends(@NonNull Long id) {

        return userStorage.getFriends(id);
    }
}
