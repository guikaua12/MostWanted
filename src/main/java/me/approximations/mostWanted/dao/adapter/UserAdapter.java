package me.approximations.mostWanted.dao.adapter;

import com.jaoow.sql.executor.adapter.SQLResultAdapter;
import me.approximations.mostWanted.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UserAdapter implements SQLResultAdapter<User> {
    @Override
    public User adaptResult(ResultSet rs) {
        try {
            String name = rs.getString("name");
            double headPrice = rs.getDouble("headPrice");
            return new User(name, headPrice);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
