package com.pegasus.platform.service.impl;

import com.pegasus.common.constants.CommonConstant;
import com.pegasus.platform.domain.Menu;
import com.pegasus.platform.domain.Role;
import com.pegasus.platform.domain.RoleMenu;
import com.pegasus.platform.repository.RoleMenuRepository;
import com.pegasus.platform.repository.RoleRepository;
import com.pegasus.platform.service.IRoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//import com.pegasus.platform.repository.RoleMenuRepository;
//import com.pegasus.platform.repository.RoleRepository;

/**
 * Created by enHui.Chen on 2019/9/28.
 */
@Service
public class RoleMenuServiceImpl implements IRoleMenuService {
    @Autowired
    private RoleMenuRepository roleMenuRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void roleMenuBatchDispatch(List<Menu> menus, Long roleId) {
        if (CollectionUtils.isEmpty(menus)) {
            return;
        }
        // 设置当前角色以及子角色OR继承角色的菜单权限
        this.setSubRoleMenu(menus, roleId);
    }

    private void setSubRoleMenu(List<Menu> menus, Long roleId) {
        // 当前角色的子角色OR继承角色
        List<Role> subRoles = roleRepository.findSubRoleByRoleId(roleId);

        if (CollectionUtils.isEmpty(subRoles)) {
            return;
        }
        subRoles.forEach(subRole -> this.setRoleMenu(menus, subRole.getId()));

    }

    private void setRoleMenu(List<Menu> menus, Long roleId) {
        List<Menu> createMenu = menus.stream()
                .filter(menu -> CommonConstant.CREATE.equals(menu.get_status()))
                .collect(Collectors.toList());
        List<Long> deleteMenuIds = menus.stream()
                .filter(menu -> CommonConstant.DELETE.equals(menu.get_status()))
                .map(menu -> menu.getId())
                .collect(Collectors.toList());
        // 新建
        if (!CollectionUtils.isEmpty(createMenu)) {
            roleMenuRepository.saveAll(buildRoleMenuByMenu(createMenu, roleId));
        }
        // 删除
        if (!CollectionUtils.isEmpty(deleteMenuIds)) {
            roleMenuRepository.deleteRoleMenuByMenuIdIsInAndRoleId(deleteMenuIds, roleId);
        }
    }

    private List<RoleMenu> buildRoleMenuByMenu(List<Menu> menus, Long roleId) {
        List<RoleMenu> roleMenus = new ArrayList<>();
        menus.forEach(menu -> {
            RoleMenu roleMenu = RoleMenu.builder()
                    .menuId(menu.getId())
                    .roleId(roleId)
                    .build();
            roleMenus.add(roleMenu);
        });
        return roleMenus;
    }

}
