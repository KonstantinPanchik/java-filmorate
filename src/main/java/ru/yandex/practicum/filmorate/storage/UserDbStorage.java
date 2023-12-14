package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.FriendStatus;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Component
public class UserDbStorage implements UserStorage {

    JdbcTemplate jdbcTemplate;


    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User addUser(User user) {
        addUserInDb(user);

        return getUserFromDbByLogin(user.getLogin());
    }

    @Override
    public User updateUser(User user) {
       if(updateUserInDb(user)==0){
           throw new UserNotFoundException("Пользователь не найден");
       }
        return getUserFromDbById(user.getId());
    }

    @Override
    public List<User> getAllUsers() {
        return getAllUsersFromDb();
    }

    @Override
    public User getUserById(long id) {
        return getUserFromDbById(id);
    }

    @Override
    public FriendStatus addFriend(long userId, long friendId) {
        return addFriendshipInDb(userId, friendId);

    }

    @Override
    public boolean deleteFriend(long userId, long friendId) {
        boolean isFriendDeleted = deleteFriendFromDb(userId, friendId);
        boolean isRowExist = isFriendSentInviteToUserBefore(userId, friendId);
        if (isRowExist) {
            setStatusAsNotAccept(userId, friendId);
        }
        return isFriendDeleted;
    }

    @Override
    public List<User> getMutualFriends(long userId, long userCompareWith) {
        return getMutualFriendsFromDb(userId, userCompareWith);
    }

    private List<User> getMutualFriendsFromDb(long userId, long userCompareWith) {
        String sql = "SELECT * FROM users " +
                "WHERE USER_ID IN ( SELECT f.FRIEND_ID " +
                " FROM friendship AS f " +
                " WHERE " +
                " f.user_id=? AND " +
                " f.friend_id IN ( SELECT fs.friend_id " +
                " FROM friendship AS fs " +
                " WHERE fs.user_id=? ));";

        List<User> mutualFriend = jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs), userId, userCompareWith);
        return mutualFriend;
    }

    @Override
    public List<User> getFriends(long userId) {
        return getFriendFromDb(userId);
    }

    private List<User> getFriendFromDb(long userId) {
        String sql = "SELECT * FROM users AS u\n" +
                "WHERE u.USER_ID IN (\tSELECT f.friend_id \n" +
                "\t\t\t\t\t\tFROM friendship AS f\n" +
                "\t\t\t\t\t\tWHERE f.user_id=?);";
        List<User> result = jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs), userId);
        return result;
    }

    private FriendStatus addFriendshipInDb(long userId, long friendId) {
        if (isFriendSentInviteToUserBefore(userId, friendId)) {
            setStatusAcceptAndAddNewRow(userId, friendId);
            return FriendStatus.ACCEPTED;
        } else {
            addRowWithStatusNotAccept(userId, friendId);
            return FriendStatus.NOT_ACCEPTED;
        }
    }

    private boolean isFriendSentInviteToUserBefore(long userId, long friendId) {
        String sqlAmountRow = "SELECT COUNT(*) FROM friendship WHERE  user_id=? AND friend_id=?;";
        int rowCount = jdbcTemplate.queryForObject(sqlAmountRow, Integer.class, friendId, userId);
        if (rowCount == 1) {
            return true;
        } else {
            return false;
        }
    }


    private boolean deleteFriendFromDb(long userId, long friendId) {
        String sql = "DELETE FROM friendship WHERE user_id=? AND friend_id=?;";
        int countRow = jdbcTemplate.update(sql, userId, friendId);
        if (countRow == 1) {
            return true;
        } else {
            return false;
        }
    }

    private void setStatusAsNotAccept(long userId, long friendId) {
        String sqlUpdateStatus = "UPDATE friendship SET status=? WHERE user_id=? AND friend_id=?;";
        jdbcTemplate.update(sqlUpdateStatus, FriendStatus.NOT_ACCEPTED.toString(), friendId, userId);
    }


    private void addRowWithStatusNotAccept(long userId, long friendId) {
        String sqlMerge = "MERGE INTO friendship (user_id,friend_id,status) VALUES(?,?,?);";
        jdbcTemplate.update(sqlMerge, userId, friendId, FriendStatus.NOT_ACCEPTED.toString());

    }

    private void setStatusAcceptAndAddNewRow(long userId, long friendId) {
        String sqlUpdateStatus = "UPDATE friendship SET status=? WHERE user_id=? AND friend_id=?;";
        jdbcTemplate.update(sqlUpdateStatus, FriendStatus.ACCEPTED.toString(), friendId, userId);

        String sqlMerge = "MERGE INTO friendship (user_id,friend_id,status) VALUES(?,?,?);";
        jdbcTemplate.update(sqlMerge, userId, friendId, FriendStatus.ACCEPTED.toString());

    }


    private void addUserInDb(User user) {
        String sql = "INSERT INTO users (name,email,login,birthday) " +
                " values(?,?,?,?);";
        int a = jdbcTemplate.update(sql
                , user.getName()
                , user.getEmail()
                , user.getLogin()
                , user.getBirthday());
    }

    private List<User> getAllUsersFromDb() {
        String sql = "SELECT * FROM users;";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));

    }

    private int updateUserInDb(User user) {
        String sql = "UPDATE users SET  email = ?, login = ?, name = ?, birthday = ? " +
                "  WHERE user_id = ? ;";
        int result = jdbcTemplate.update(sql
                , user.getEmail()
                , user.getLogin()
                , user.getName()
                , user.getBirthday()
                , user.getId());
        return result;
    }

    private User getUserFromDbById(Long userId) {
        String sql = "SELECT * FROM users WHERE user_id=?";
        User user = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> makeUser(rs), userId);
        return user;
    }

    private User getUserFromDbByLogin(String login) {
        String sql = "SELECT * FROM users WHERE login=?";
        User user = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> makeUser(rs), login);
        return user;
    }

    private User makeUser(ResultSet resultSet) throws SQLException {
        User user = new User(resultSet.getString("email")
                , LocalDate.parse(resultSet.getString("birthday")));
        user.setName(resultSet.getString("name"));
        user.setLogin(resultSet.getString("login"));
        user.setId(resultSet.getLong("user_id"));
        return user;
    }

}
