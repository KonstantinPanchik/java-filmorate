package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.FriendStatus;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    User addUser(User user);

    User updateUser(User user);

    List<User> getAllUsers();

    User getUserById(long id);

    FriendStatus addFriend(long userId, long friendId);

    boolean deleteFriend(long userId, long friendId);

    List<User> getMutualFriends(long userId, long userCompareWith);
    List<User> getFriends(long userId);

}
