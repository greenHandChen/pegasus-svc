package com.pegasus.platform.service;

import com.pegasus.platform.domain.Menu;

import java.util.List;
import java.util.Set;

/**
 * Created by enHui.Chen on 2019/8/30.
 */
public interface IMenuService {
    List<Menu> findMenuAll();

    List<Menu> initMenuByRoleId(Long roleId);

    Menu createOrUpdateMenu(Menu menu);

    List<Menu> findMenuById(Long id);

    List<Menu> findTiledMenuByRoleId(Long roleId);

    List<Menu> findMenuByMenuName(String name);
}
