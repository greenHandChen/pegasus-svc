package com.pegasus.platform.service;

import com.pegasus.platform.domain.Menu;

import java.util.List;

/**
 * Created by enHui.Chen on 2019/9/28.
 */
public interface IRoleMenuService {
    void roleMenuBatchDispatch(List<Menu> menus, Long roleId);
}
