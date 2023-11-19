package ru.yandex.practicum.filmorate.servise;


import lombok.NonNull;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

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

    public List<User> getMutualFriends(@NonNull User user, @NotNull User friend, @NotNull UserStorage userStorage) {
        List<Long> mutualFriends = new ArrayList<>(user.getFriends());
        mutualFriends.retainAll(friend.getFriends());

        return mutualFriends.stream().map(l ->
                userStorage.getUserById(l)).collect(Collectors.toList());
    }

    public List<User> getFriends(@NonNull User user, @NonNull UserStorage userStorage) {
        List<User> result = user.getFriends().stream()
                .map(f -> userStorage.getUserById(f))
                .collect(Collectors.toList());
        return result;
    }
}
