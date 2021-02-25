package com.pegasus.platform.service;

import com.pegasus.platform.domain.Role;

import java.util.List;

/**
 * Created by enHui.Chen on 2019/9/5.
 */
public interface IRoleService {
    List<Role> findRolesByUserId(Long userId);

    List<Role> findAccountAllocatableRole(Long userId);

    List<Role> findRoleAll();

    List<Role> findRoleAllExcludeAdmin();

    List<Role> findRoleListByUserId(Long userId);

    Role roleCreateOrEdit(Role role);

    Role roleCopyOrExtend(Role role);

    Role roleActive(Role role);
}
