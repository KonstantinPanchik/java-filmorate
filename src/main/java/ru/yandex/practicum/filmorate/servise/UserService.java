package ru.yandex.practicum.filmorate.servise;


import lombok.Data;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Data
public class UserService {

    @NonNull
    UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public boolean addFriend(@NonNull User user, @NonNull User friend) {
        user.getFriends().add(friend.getId());
        friend.getFriends().add(user.getId());
        return true;
    }

    public boolean removeFriend(@NonNull User user, @NonNull User friend) {
        user.getFriends().remove(friend.getId());
        friend.getFriends().remove(user.getId());
        return true;
    }

    public List<User> getMutualFriends(@NonNull User user, @NotNull User friend) {
        List<Long> mutualFriends = new ArrayList<>(user.getFriends());
        mutualFriends.retainAll(friend.getFriends());

        return mutualFriends.stream().map(l ->
                userStorage.getUserById(l)).collect(Collectors.toList());
    }

    public List<User> getFriends(@NonNull Long id) {
        List<User> result = userStorage.getUserById(id).getFriends().stream()
                .map(f -> userStorage.getUserById(f))
                .collect(Collectors.toList());
        return result;
    }
}
