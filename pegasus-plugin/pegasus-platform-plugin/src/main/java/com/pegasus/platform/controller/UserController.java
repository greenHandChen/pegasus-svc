package com.pegasus.platform.controller;

import com.pegasus.platform.domain.User;
import com.pegasus.platform.service.IUserRoleService;
import com.pegasus.platform.service.IUserService;
import com.pegasus.platform.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by enHui.Chen on 2019/9/5.
 */
@RestController
@RequestMapping("/v1/user")
public class UserController {
    @Autowired
    private IUserService userService;
    @Autowired
    private IUserRoleService userRoleService;

    /**
     * @Author: enHui.Chen
     * @Description: 获取当前登录用户
     * @Data 2019/9/5
     */
    @GetMapping("/self")
    public UserVO getCurrentUser() {
        return userService.getCurrentUser();
    }

    /**
     * @Author: enHui.Chen
     * @Description: 获取所有用户的账户信息
     * @Data 2019/10/16
     */
    @GetMapping("/findAccountAll")
    public ResponseEntity<List<User>> findAccountAll() {
        return ResponseEntity.ok(userService.findAccountAll());
    }

    /**
     * @Author: enHui.Chen
     * @Description: 创建/编辑帐号
     * @Data 2019/10/16
     */
    @PostMapping("/createOrUpdateAccount")
    public ResponseEntity createOrUpdateAccount(@RequestBody User user) {
        userService.createOrUpdateAccount(user);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    /**
     * @Author: enHui.Chen
     * @Description: 根据UserId查找帐号信息
     * @Data 2019/10/16
     */
    @GetMapping("/findAccountByUserId")
    public ResponseEntity findAccountByUserId(Long id) {
        User user = userService.findAccountByUserId(id);
        return ResponseEntity.ok(user);
    }

    /**
     * @Author: enHui.Chen
     * @Description: 帐号角色分配
     * @Data 2019/10/16
     */
    @PostMapping("/dispatchRole/{userId}")
    public ResponseEntity dispatchRole(@PathVariable(name = "userId") Long userId,
                                       @RequestBody List<Long> roleIds) {
        userRoleService.createUserRole(userId, roleIds);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    /**
     * @Author: enHui.Chen
     * @Description: 帐号角色删除
     * @Data 2019/10/16
     */
    @DeleteMapping("/deleteDispatchRole/{userId}")
    public ResponseEntity deleteDispatchRole(@PathVariable(name = "userId") Long userId,
                                             @RequestBody List<Long> roleIds) {
        userRoleService.deleteDispatchRole(userId, roleIds);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    /**
     * @Author: enHui.Chen
     * @Description: 修改密码
     * @Data 2019/10/16
     */
    @PutMapping("/modifyPassword")
    public ResponseEntity modifyPassword(Long id, String oldPassword, String password) {
        userService.modifyPassword(id, oldPassword, password);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
