package com.pegasus.platform.controller;

import com.pegasus.platform.domain.Menu;
import com.pegasus.platform.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by enHui.Chen on 2019/8/30.
 */
@RestController
@RequestMapping("/v1/menu")
public class MenuController {
    @Autowired
    private IMenuService menuService;

    /**
     * @Author: enHui.Chen
     * @Description: 获取所有菜单
     * @Data 2019/9/6
     */
    @GetMapping("/findMenuAll")
    public ResponseEntity<List<Menu>> findMenuAll() {
        List<Menu> all = menuService.findMenuAll();
        return ResponseEntity.ok(all);
    }

    /**
     * @Author: enHui.Chen
     * @Description: 根据角色ID获取菜单
     * @Data 2019/9/6
     */
    @GetMapping("/initMenuByRoleId")
    public ResponseEntity<List<Menu>> initMenuByRoleId(Long roleId) {
        List<Menu> all = menuService.initMenuByRoleId(roleId);
        return ResponseEntity.ok(all);
    }

    /**
     * @Author: enHui.Chen
     * @Description: 新建/更新菜单
     * @Data 2019/9/26
     */
    @PostMapping("/createOrUpdate/menu")
    public ResponseEntity createOrUpdateMenu(@RequestBody Menu menu) {
        return ResponseEntity.ok(menuService.createOrUpdateMenu(menu));
    }

    /**
     * @Author: enHui.Chen
     * @Description: 启用/禁用菜单
     * @Data 2019/9/26
     */
    @PostMapping("/activeOrForbid/menu")
    public ResponseEntity activeOrForbidMenu(Menu menu) {
        return ResponseEntity.ok(menuService.createOrUpdateMenu(menu));
    }

    /**
     * @Author: enHui.Chen
     * @Description: 根据菜单ID获取菜单详情
     * @Data 2019/9/26
     */
    @GetMapping("/findMenuById")
    public ResponseEntity findMenuById(@RequestParam(name = "id") Long id) {
        return ResponseEntity.ok(menuService.findMenuById(id));
    }

    /**
     * @Author: enHui.Chen
     * @Description: 根据RoleID获取平铺菜单数据
     * @Data 2019/9/26
     */
    @GetMapping("/findTiledMenuByRoleId")
    public ResponseEntity findTiledMenuByRoleId(@RequestParam(name = "roleId") Long roleId) {
        return ResponseEntity.ok(menuService.findTiledMenuByRoleId(roleId));
    }

    /**
     * @Author: enHui.Chen
     * @Description: 根据菜单名模糊查询菜单树
     * @Data 2019/9/6
     */
    @GetMapping("/findMenuByMenuName")
    public ResponseEntity<List<Menu>> findMenuByMenuName(@RequestParam(name = "name", required = false) String name) {
        List<Menu> all = menuService.findMenuByMenuName(name);
        return ResponseEntity.ok(all);
    }
}
