package com.gh.pegasus.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.jdbc.core.RowMapper;

import javax.persistence.Column;
import javax.persistence.Id;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by enHui.Chen on 2019/8/30.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User implements RowMapper<User> {
    @Id
    private Long id;
    @Column
    private String userType;
    @Column
    private String username;
    @Column
    private String password;
    @Column
    private String nickName;
    @Column
    private String email;
    @Column
    private String phone;
    @Column
    private Boolean isActive;

    private Boolean isAuth;

    @Override
    public User mapRow(ResultSet resultSet, int i) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong("id"));
        user.setUsername(resultSet.getString("username"));
        user.setUserType(resultSet.getString("user_type"));
        user.setPassword(resultSet.getString("password"));
        user.setNickName(resultSet.getString("nick_name"));
        user.setEmail(resultSet.getString("email"));
        user.setPhone(resultSet.getString("phone"));
        user.setIsAuth(resultSet.getBoolean("is_active"));
        return user;
    }
}
