package com.pegasus.platform.controller;

import com.pegasus.platform.domain.Menu;
import com.pegasus.platform.domain.Role;
import com.pegasus.platform.service.IRoleMenuService;
import com.pegasus.platform.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by enHui.Chen on 2019/9/27.
 */
@RestController
@RequestMapping("/v1/role")
public class RoleController {
    @Autowired
    private IRoleService roleService;
    @Autowired
    private IRoleMenuService roleMenuService;

    /**
     * @Author: enHui.Chen
     * @Description: 获取所有角色
     * @Data 2019/9/27
     */
    @GetMapping("/findRoleAll")
    public ResponseEntity<List<Role>> findRoleAll() {
        return ResponseEntity.ok(roleService.findRoleAll());
    }

    /**
     * @Author: enHui.Chen
     * @Description: 获取所有角色(不含管理员)
     * @Data 2019/9/27
     */
    @GetMapping("/findRoleAll/excludeAdmin")
    public ResponseEntity<List<Role>> findRoleAllExcludeAdmin() {
        return ResponseEntity.ok(roleService.findRoleAllExcludeAdmin());
    }

    /**
     * @Author: enHui.Chen
     * @Description: 根据userId获取角色信息
     * @Data 2019/9/27
     */
    @GetMapping("/findRoleListByUserId")
    public ResponseEntity<List<Role>> findRoleListByUserId(Long userId) {
        return ResponseEntity.ok(roleService.findRoleListByUserId(userId));
    }

    /**
     * @Author: enHui.Chen
     * @Description: 批量权限分配
     * @Data 2019/9/28
     */
    @PostMapping("/roleMenu/batchDispatch/{roleId}")
    public ResponseEntity roleMenuBatchDispatch(@RequestBody List<Menu> menus,
                                                @PathVariable("roleId") Long roleId) {
        roleMenuService.roleMenuBatchDispatch(menus, roleId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    /**
     * @Author: enHui.Chen
     * @Description: 创建/编辑角色
     * @Data 2019/9/29
     */
    @PostMapping("/roleCreateOrEdit")
    public ResponseEntity roleCreateOrEdit(@RequestBody Role role) {
        roleService.roleCreateOrEdit(role);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    /**
     * @Author: enHui.Chen
     * @Description: 复制/继承角色(角色拥当前角色原有菜单)
     * @Data 2019/9/29
     */
    @PostMapping("/roleCopyOrExtend")
    public ResponseEntity roleCopyOrExtend(@RequestBody Role role) {
        roleService.roleCopyOrExtend(role);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    /**
     * @Author: enHui.Chen
     * @Description: 激活/禁用角色
     * @Data 2019/9/29
     */
    @PutMapping("/roleActive")
    public ResponseEntity roleActive(@RequestBody Role role) {
        roleService.roleActive(role);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    /**
     * @Author: enHui.Chen
     * @Description: 获取账户可分配角色
     * @Data 2019/9/27
     */
    @GetMapping("/findAccountAllocatableRole/{userId}")
    public ResponseEntity<List<Role>> findAccountAllocatableRole(@PathVariable(name = "userId") Long userId) {
        return ResponseEntity.ok(roleService.findAccountAllocatableRole(userId));
    }
}
