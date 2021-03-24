package com.gh.pegasus.service.impl;

import com.gh.pegasus.domain.User;
import com.gh.pegasus.security.UsernamePasswordSubSystemCredential;
import com.gh.pegasus.service.IUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by enHui.Chen on 2021/2/26.
 */
@Slf4j
@Service
public class UserDetailsServiceImpl implements IUserDetailsService {
    private static final String USER_TABLE = "pe_user";
    private static final String USER_AUTH_SQL = "select * from " + USER_TABLE + " where username = ? and password = ?;";

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Override
    public User authenticate(UsernamePasswordSubSystemCredential upssc) {
        String username = upssc.getUsername();
        String password = upssc.getPassword();
        String captcha = upssc.getCaptcha();
        try {
            return jdbcTemplate.queryForObject(USER_AUTH_SQL, new Object[]{username, password}, new User());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
