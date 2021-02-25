package com.pegasus.platform.service;

import com.pegasus.platform.domain.UserRole;

import java.util.List;

/**
 * Created by enHui.Chen on 2019/10/18.
 */
public interface IUserRoleService {
    void createUserRole(Long userId,List<Long> roleIds);

    void deleteDispatchRole(Long userId,List<Long> roleIds);

    List<UserRole> findByUserId(Long userId);
}
