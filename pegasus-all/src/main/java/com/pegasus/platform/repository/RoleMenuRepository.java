package com.pegasus.platform.repository;

import com.pegasus.platform.domain.RoleMenu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author: enHui.Chen
 * @Description:
 * @Data 2019/11/8
 */
public interface RoleMenuRepository extends JpaRepository<RoleMenu, Long> {
    int deleteRoleMenuByMenuIdIsInAndRoleId(List<Long> menuIds, Long roleId);

    List<RoleMenu> findByRoleId(Long roleId);
}
