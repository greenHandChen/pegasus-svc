package com.pegasus.platform.repository;

import com.pegasus.platform.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

/**
 * @Author: enHui.Chen
 * @Description:
 * @Data 2019/11/8
 */
public interface MenuRepository extends JpaRepository<Menu, Long>, JpaSpecificationExecutor {

    @Query(nativeQuery = true,
            value = "SELECT " +
                    " pm.* " +
                    "FROM " +
                    " pe_role_menu prm, " +
                    " pe_menu pm " +
                    "WHERE " +
                    " pm.id = prm.menu_id " +
                    "AND prm.role_id = :roleId " +
                    "ORDER BY pm.sort ")
    List<Menu> initMenuByRoleId(Long roleId);

    List<Menu> findMenuById(Long id);

    @Query(nativeQuery = true,
            value = "SELECT " +
                    " pm.* " +
                    "FROM " +
                    " pe_menu pm " +
                    "WHERE " +
                    "pm.name like concat('%',concat(:name,'%')) " +
                    "ORDER BY pm.sort ")
    List<Menu> findByNameLike(String name);

    List<Menu> findByIdIn(Set<Long> ids);
}
