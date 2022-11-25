package me.approximations.mostWanted.dao;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.approximations.mostWanted.dao.repository.UserRepository;
import me.approximations.mostWanted.model.User;
import org.bukkit.plugin.Plugin;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public class UserDao {
    @Getter
    private final UserRepository userRepository;
    private final Plugin plugin;
    @Getter
    private final Map<String, User> users = new LinkedHashMap<>();
    public void insertOrUpdate(User user) {
        if(contains(user.getName())) {
            update(user);
        }else {
            insert(user);
        }
    }

    public void insert(User user) {
        users.put(user.getName(), user);
    }

    public void update(User user) {
        users.replace(user.getName(), user);
    }

    public void remove(String name) {
        users.remove(name);
    }

    public boolean contains(String name) {
        return users.containsKey(name);
    }

    public void getAll() {
        userRepository.getAll().forEach(user -> {
            insertOrUpdate(user);
        });
    }

    public void saveAll() {
        users.forEach((nick, user) -> {
            userRepository.insertOrUpdate(user);
        });
    }

    public void saveOne(User user) {
        userRepository.update(user);
    }
}