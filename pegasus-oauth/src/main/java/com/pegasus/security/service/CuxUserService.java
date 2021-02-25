package com.pegasus.security.service;

import com.pegasus.platform.domain.Role;
import com.pegasus.platform.domain.User;
import com.pegasus.platform.service.IRoleService;
import com.pegasus.platform.service.IUserService;
import com.pegasus.security.dto.CuxUserDetails;
import com.pegasus.security.exception.RoleNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by enHui.Chen on 2018/3/22 0022.
 */
@Component
public class CuxUserService implements UserDetailsService {
    @Autowired
    private IUserService userService;
    @Autowired
    private IRoleService roleService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException, RoleNotFoundException {
        User user = userService.findByUsername(userName);

        if (user == null) {
            throw new UsernameNotFoundException("未找到该用户！");
        }

        List<Role> roles = roleService.findRolesByUserId(user.getId());

        if (CollectionUtils.isEmpty(roles)) {
            throw new RoleNotFoundException("当前用户未分配角色!");
        }

        List<SimpleGrantedAuthority> simpleGrantedAuthorities = generateUserAuthority(roles);

        CuxUserDetails customUserDetails = new CuxUserDetails(user.getUsername(), user.getPassword(), simpleGrantedAuthorities);
        customUserDetails.setUserId(user.getId());
        customUserDetails.setPhone(user.getPhone());
        customUserDetails.setEmail(user.getEmail());
        customUserDetails.setIsActive(user.getIsActive());
        customUserDetails.setRoleId(roles.get(0).getId());
        customUserDetails.setRoleIds(roles.stream().map(Role::getId).collect(Collectors.toList()));
        return customUserDetails;
    }

    private List<SimpleGrantedAuthority> generateUserAuthority(List<Role> roles) {
        List<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();
        roles.forEach(role -> simpleGrantedAuthorities.add(new SimpleGrantedAuthority(role.getRoleName())));
        return simpleGrantedAuthorities;
    }

}
