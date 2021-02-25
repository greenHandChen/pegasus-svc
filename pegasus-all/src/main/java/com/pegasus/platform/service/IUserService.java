package com.pegasus.platform.service;

import com.pegasus.platform.domain.User;
import com.pegasus.platform.vo.UserVO;

import java.util.List;

/**
 * Created by enHui.Chen on 2019/9/4.
 */
public interface IUserService {
    User findByUsername(String username);

    UserVO getCurrentUser();

    List<User> findAccountAll();

    void createOrUpdateAccount(User user);

    User findAccountByUserId(Long userId);

    void modifyPassword(Long userId, String oldPassword, String newPassword);
}
