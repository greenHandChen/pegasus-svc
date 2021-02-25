package com.pegasus.platform.service.impl;

import com.pegasus.platform.domain.UserRole;
import com.pegasus.platform.repository.UserRoleRepository;
import com.pegasus.platform.service.IUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//import com.pegasus.platform.repository.UserRoleRepository;

/**
 * Created by enHui.Chen on 2019/10/18.
 */
@Service
public class UserRoleServiceImpl implements IUserRoleService {
    @Autowired
    private UserRoleRepository userRoleRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createUserRole(Long userId, List<Long> roleIds) {
        // 找到原有角色
        List<UserRole> userRoleList = userRoleRepository.findByUserId(userId);
        // 对已有角色进行过滤
        List<Long> filterUserRoles = roleIds;
        if (!CollectionUtils.isEmpty(userRoleList)) {
            List<Long> oldRoleIds = userRoleList.stream().map(UserRole::getRoleId).collect(Collectors.toList());
            filterUserRoles = filterUserRoles.stream().filter(userRole -> !oldRoleIds.contains(userRole)).collect(Collectors.toList());
        }
        if (userId != null) {
            List<UserRole> saveUserRoles = new ArrayList<>();
            filterUserRoles.forEach(roleId -> {
                UserRole save = UserRole.builder()
                        .userId(userId)
                        .roleId(roleId)
                        .build();
                saveUserRoles.add(save);
            });
            userRoleRepository.saveAll(saveUserRoles);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDispatchRole(Long userId, List<Long> roleIds) {
        if (CollectionUtils.isEmpty(roleIds) || userId == null) {
            return;
        }
        roleIds.forEach(roleId -> userRoleRepository.deleteDispatchRole(userId, roleId));
    }

    @Override
    public List<UserRole> findByUserId(Long userId) {
        return userRoleRepository.findByUserId(userId);
    }


}
