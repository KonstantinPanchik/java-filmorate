package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

@Data
public class FriendShip {
    private int userId;
    private int friendId;
    @NonNull
    private FriendStatus status;


}
