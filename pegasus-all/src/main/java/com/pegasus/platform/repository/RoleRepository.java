package com.pegasus.platform.repository;

import com.pegasus.platform.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @Author: enHui.Chen
 * @Description:
 * @Data 2019/11/8
 */
public interface RoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor {

    @Query(nativeQuery = true,
            value = "select fr.* from pe_role fr,pe_user_role fur where fur.role_id = fr.id and fur.user_id = :userId AND fr.is_active = 1")
    List<Role> findRolesByUserId(Long userId);

    @Query(value = "SELECT new com.pegasus.platform.domain.Role(" +
            "pr.id," +
            "pr.roleCode," +
            "pr.roleName," +
            "pr.parentId," +
            "pr.parentLevelPath," +
            "pr.extendId," +
            "pr.extendLevelPath," +
            "pr.isActive," +
            "pr1.roleName," +
            "pr2.roleName)  " +
            "FROM " +
            " Role pr " +
            "LEFT JOIN Role pr1 ON pr1.id = pr.parentId " +
            "LEFT JOIN Role pr2 ON pr2.id = pr.extendId " +
            "order by pr.id ")
    List<Role> findRolesAll();

    @Query(value = "SELECT new com.pegasus.platform.domain.Role(" +
            "pr.id," +
            "pr.roleCode," +
            "pr.roleName," +
            "pr.parentId," +
            "pr.parentLevelPath," +
            "pr.extendId," +
            "pr.extendLevelPath," +
            "pr.isActive," +
            "pr1.roleName," +
            "pr2.roleName)  " +
            "FROM User user, " +
            " UserRole userRole, " +
            " Role pr " +
            "LEFT JOIN Role pr1 ON pr1.id = pr.parentId " +
            "LEFT JOIN Role pr2 ON pr2.id = pr.extendId " +
            "WHERE user.id = userRole.userId " +
            "AND userRole.roleId = pr.id " +
            "AND user.id = :userId " +
            "order by pr.id ")
    List<Role> findRoleListByUserId(Long userId);

    @Query(value = "SELECT new com.pegasus.platform.domain.Role(" +
            "pr.id," +
            "pr.roleCode," +
            "pr.roleName," +
            "pr.parentId," +
            "pr.parentLevelPath," +
            "pr.extendId," +
            "pr.extendLevelPath," +
            "pr.isActive," +
            "pr1.roleName," +
            "pr2.roleName)  " +
            "FROM " +
            " Role pr " +
            "LEFT JOIN Role pr1 ON pr1.id = pr.parentId " +
            "LEFT JOIN Role pr2 ON pr2.id = pr.extendId " +
            "where pr.roleCode <> 'ROLE_ADMIN' " +
            "order by pr.id ")
    List<Role> findRoleAllExcludeAdmin();

    @Query(nativeQuery = true,
            value = "SELECT " +
                    " pr.* " +
                    "FROM " +
                    " pe_role pr " +
                    "WHERE " +
                    " pr.extend_level_path LIKE concat('%/', :roleId, '/%') " +
                    "OR pr.parent_level_path LIKE concat('%/', :roleId, '/%') " +
                    "OR pr.extend_id = :roleId " +
                    "OR pr.parent_id = :roleId " +
                    "OR pr.id = :roleId ")
    List<Role> findSubRoleByRoleId(Long roleId);

    @Query(nativeQuery = true,
            value = "SELECT t.* FROM( SELECT " +
                    " pr.* " +
                    "FROM " +
                    " pe_role pr " +
                    "WHERE " +
                    " pr.extend_level_path LIKE concat('%/', :roleId, '/%') " +
                    "OR pr.parent_level_path LIKE concat('%/', :roleId, '/%') " +
                    "OR pr.extend_id = :roleId " +
                    "OR pr.parent_id = :roleId " +
                    "OR pr.id = :roleId)t " +
                    "WHERE " +
                    " t.id NOT IN (:roleIds) ")
    List<Role> findAccountAllocatableRole(Long roleId, List<Long> roleIds);
}
