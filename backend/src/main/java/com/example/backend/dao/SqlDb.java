package com.example.backend.dao;
import com.example.backend.entities.Image;
import com.example.backend.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SqlDb implements DbRepository{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public int saveUser(User user) {

        return jdbcTemplate.update(
                "insert into Users (userid, name, email) values(?,?,?)",
                user.getUserID(), user.getName(), user.getEmail());
    }

    @Override
    public int saveImage(Image image) {

        return jdbcTemplate.update(
                "insert into Images (userid, imageData) values(?,?)",
                image.getUserID(), image.getImageData());

    }

    @Override
    public User getUser(String userId) {
        String sql = "SELECT * FROM Users WHERE userid = ?";
        User target = jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                new User(
                        rs.getString("userid"),
                        rs.getString("name"),
                        rs.getString("email")
                ), new Object[] { userId });
        return target;
    }

    @Override
    public Image getImage(String userId) {
        String sql = "SELECT * FROM Images WHERE userid = ?";
        Image target = jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                new Image(
                        rs.getString("userid"),
                        rs.getBytes("imageData")
                ),new Object[]{userId});
        return target;

    }
}