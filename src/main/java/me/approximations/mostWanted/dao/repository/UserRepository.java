package me.approximations.mostWanted.dao.repository;

import com.jaoow.sql.executor.SQLExecutor;
import com.jaoow.sql.executor.batch.BatchBuilder;
import me.approximations.mostWanted.Main;
import me.approximations.mostWanted.model.User;
import org.bukkit.event.player.PlayerFishEvent;

import java.sql.SQLException;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class UserRepository{
    private final Main plugin;
    private final SQLExecutor sqlExecutor;
    private final String TABLE;

    public UserRepository(Main plugin, SQLExecutor sqlExecutor) {
        this.plugin = plugin;
        this.sqlExecutor = sqlExecutor;
        this.TABLE = plugin.getConfig().getString("database.table");
    }



    public void createTable() {
        sqlExecutor.executeAsync("CREATE TABLE IF NOT EXISTS "+TABLE+"(name VARCHAR(35) PRIMARY KEY NOT NULL, headPrice DOUBLE);");
    }

    public void insertOrUpdate(User user) {
        User user1 = get(user.getName());
        if(user1 != null) {
            update(user);
        }else {
            insert(user);
        }
    }

    public void insert(User user) {
        sqlExecutor.execute("INSERT INTO "+TABLE+" VALUES(?, ?);", c -> {
            try {
                c.setString(1, user.getName());
                c.setDouble(2, user.getHeadPrice());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void update(User user) {
        sqlExecutor.executeAsync("UPDATE "+TABLE+" SET headPrice = ? WHERE name = ?;", c -> {
            try {
                c.setDouble(1, user.getHeadPrice());
                c.setString(2, user.getName());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

    }

    public User get(String name) {
        return sqlExecutor.query("SELECT * FROM "+TABLE+" WHERE name = ?;", c -> {
            try {
                c.setString(1, name);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }, User.class).orElse(null);
    }

    public boolean contains(String name) {
        return get(name) != null;
    }

    public void delete(String name) {
        sqlExecutor.execute("DELETE FROM "+TABLE+" WHERE name = ?;", c -> {
            try {
                c.setString(1, name);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public Set<User> getAll() {
        return sqlExecutor.queryMany("SELECT * FROM "+TABLE+";", User.class);
    }

    public Set<User> getTop10() {
        return sqlExecutor.queryMany("SELECT * FROM "+TABLE+" ORDER BY headPrice DESC LIMIT 10;", User.class);
    }
}
