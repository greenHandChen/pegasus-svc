package com.pegasus.platform.service.impl;

import com.pegasus.platform.constants.MenuConstant;
import com.pegasus.platform.domain.Menu;
import com.pegasus.platform.repository.MenuRepository;
import com.pegasus.platform.service.IMenuService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

//import com.pegasus.platform.repository.MenuRepository;

/**
 * Created by enHui.Chen on 2019/8/30.
 */
@Service
public class MenuServiceImpl implements IMenuService {
    @Autowired
    private MenuRepository menuRepository;

    @Override
    public List<Menu> findMenuAll() {
        List<Menu> menuTrees = menuRepository.findAll();
        generateMenuTree(menuTrees);
        // 过滤出一级菜单
        menuTrees = menuTrees.stream().filter(menu -> menu.getParentId().compareTo(0L) == 0).collect(Collectors.toList());
        return menuTrees;
    }

    @Override
    public List<Menu> initMenuByRoleId(Long roleId) {
        List<Menu> menuTrees = menuRepository.initMenuByRoleId(roleId);

        generateMenuTree(menuTrees);
        // 过滤出一级菜单
        menuTrees = menuTrees.stream().filter(menu -> menu.getParentId().compareTo(0L) == 0).collect(Collectors.toList());
        return menuTrees;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Menu createOrUpdateMenu(Menu menu) {
        String menuType = menu.getType();
        menu.setParentId(MenuConstant.MENU_ROOT_DIRECTORY.equals(menuType) ? 0L : menu.getParentId());
        menu.setPath(MenuConstant.MENU_FUNC_MENU.equals(menuType) ? menu.getPath() : "");
        menu.setIsLeaf(MenuConstant.MENU_FUNC_MENU.equals(menuType));
        menu.setIsActive(menu.getIsActive() == null ? true : menu.getIsActive());
        menu.setLeftClass(StringUtils.isNotEmpty(menu.getLeftClass()) ? menu.getLeftClass() : MenuConstant.DEFAULT_LEFT_CLASS);
        menu = menuRepository.save(menu);
        return menu;
    }

    @Override
    public List<Menu> findMenuById(Long id) {
        return menuRepository.findMenuById(id);
    }

    @Override
    public List<Menu> findTiledMenuByRoleId(Long roleId) {
        return menuRepository.initMenuByRoleId(roleId);
    }

    @Override
    public List<Menu> findMenuByMenuName(String name) {
        List<Menu> menus;
        if (StringUtils.isNotEmpty(name)) {
            List<Menu> menuList = menuRepository.findByNameLike(name);
            Set<String> menuIds = new HashSet<>();
            // 获取当前菜单ID以及当前菜单父级ID
            menuList.forEach(menu -> {
                Set<String> tempMenuIds = new HashSet<>(Arrays.asList(menu.getLevelPath().split("/")));
                menuIds.add(String.valueOf(menu.getId()));
                menuIds.addAll(tempMenuIds);
            });
            if (CollectionUtils.isEmpty(menuIds)) {
                return null;
            }
            menus = menuRepository.findByIdIn(menuIds.stream().map(menuId -> Long.valueOf(menuId)).collect(Collectors.toSet()));
        } else {
            menus = findMenuAll();
        }
        generateMenuTree(menus);
        // 过滤出一级菜单
        menus = menus.stream().filter(menu -> menu.getParentId().compareTo(0L) == 0).collect(Collectors.toList());
        return menus;
    }

    /**
     * @Author: enHui.Chen
     * @Description: 生成菜单树
     * @Data 2019/8/30
     */
    private void generateMenuTree(List<Menu> menuTrees) {
        if (CollectionUtils.isEmpty(menuTrees)) {
            return;
        }
        menuTrees.forEach(menu -> {
            if (menu.getIsLeaf()) {
                return;
            }
            List<Menu> routerData = new ArrayList<>();
            menuTrees.forEach(subMenu -> {
                if (menu.getId().compareTo(subMenu.getParentId()) == 0) {
                    routerData.add(subMenu);
                    menu.setRouterData(routerData);
                }
            });
        });
    }
}
