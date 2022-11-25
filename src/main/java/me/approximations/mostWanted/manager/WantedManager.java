package me.approximations.mostWanted.manager;

import lombok.RequiredArgsConstructor;
import me.approximations.mostWanted.dao.UserDao;
import me.approximations.mostWanted.model.User;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class WantedManager {
    private final UserDao userDao;

    public List<User> getWantedUsers() {
        return userDao.getUsers().values().stream().filter(User::isInContract).collect(Collectors.toList());
    }
}
