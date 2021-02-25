package com.pegasus.platform.service.impl;

import com.pegasus.common.dto.CuxUserDetails;
import com.pegasus.common.utils.SecurityHelper;
import com.pegasus.platform.domain.Role;
import com.pegasus.platform.domain.RoleMenu;
import com.pegasus.platform.domain.UserRole;
import com.pegasus.platform.repository.RoleMenuRepository;
import com.pegasus.platform.repository.RoleRepository;
import com.pegasus.platform.service.IRoleService;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by enHui.Chen on 2019/9/5.
 */
@Service
public class RoleServiceImpl implements IRoleService {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RoleMenuRepository roleMenuRepository;
    @Autowired
    private UserRoleServiceImpl userRoleService;

    @Override
    public List<Role> findRolesByUserId(Long userId) {
        return roleRepository.findRolesByUserId(userId);
    }

    @Override
    public List<Role> findAccountAllocatableRole(Long userId) {
        CuxUserDetails userDetails = SecurityHelper.getCurrentUser();
        Assert.notNull(userDetails, "获取账户可分配角色时,当前用户不能为空!");
        Long currentRoleId = userDetails.getRoleId();
        // 获取已分配的角色ID
        List<UserRole> userRoleList = userRoleService.findByUserId(userId);
        List<Long> userRoleId = null;
        if (!CollectionUtils.isEmpty(userRoleList)) {
            userRoleId = userRoleList.stream().map(UserRole::getRoleId).collect(Collectors.toList());
        }
        if (CollectionUtils.isEmpty(userRoleId)) {
            return roleRepository.findSubRoleByRoleId(currentRoleId);

        }
        return roleRepository.findAccountAllocatableRole(currentRoleId, userRoleId);
    }

    @Override
    public List<Role> findRoleAll() {
        return roleRepository.findRolesAll();

    }

    @Override
    public List<Role> findRoleAllExcludeAdmin() {
        return roleRepository.findRoleAllExcludeAdmin();
    }

    @Override
    public List<Role> findRoleListByUserId(Long userId) {
        return roleRepository.findRoleListByUserId(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Role roleCreateOrEdit(Role role) {
        Role saveRole = new Role();
        BeanUtils.copyProperties(role, saveRole);
        saveRole.setIsActive(true);
        roleRepository.save(saveRole);
        return saveRole;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Role roleCopyOrExtend(Role role) {
        Long roleId = role.getId();
        role.setId(null);
        Role saveRole = ((IRoleService) AopContext.currentProxy()).roleCreateOrEdit(role);
        List<RoleMenu> roleMenus = roleMenuRepository.findByRoleId(roleId);
        if (CollectionUtils.isEmpty(roleMenus)) {
            return saveRole;
        }
        List<RoleMenu> saveRoleMenus = new ArrayList<>();
        roleMenus.forEach(roleMenu -> {
            RoleMenu saveRoleMenu = RoleMenu.builder()
                    .roleId(saveRole.getId())
                    .menuId(roleMenu.getMenuId())
                    .build();
            saveRoleMenus.add(saveRoleMenu);
        });
        roleMenuRepository.saveAll(saveRoleMenus);
        return saveRole;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Role roleActive(Role role) {
        Role activeRole = roleRepository.findById(role.getId()).get();
        activeRole.setIsActive(activeRole.getIsActive() == null || !activeRole.getIsActive());
        roleRepository.save(activeRole);
        return activeRole;
    }
}
