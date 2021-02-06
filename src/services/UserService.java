package services;

import exceptions.ResourceNotFoundException;
import models.User;

import java.rmi.NoSuchObjectException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Stream;

public class UserService {

    private static Map<UUID, User> userMap = new HashMap<>();

    public static Stream<User> getUsers() {
        return userMap.values().stream();
    }

    public static void addUser(User newUser) {
        userMap.put(newUser.getId(), newUser);
    }

    public static boolean checkUserLevel(UUID userId, User.Level userLevel) {
        User user = userMap.get(userId);
        if(user == null) {
            throw new NoSuchElementException();
        }
        return user.getUserLevel() == userLevel;
    }

    public static boolean upgradeUserLevel(UUID userId) {
        User user = userMap.get(userId);
        if (user == null) {
            return false;
        }
        User.Level nextLevel = User.Level.getLevel(user.getUserLevel().getOrder() + 1);
        user.setUserLevel(nextLevel);
        return true;
    }
}
