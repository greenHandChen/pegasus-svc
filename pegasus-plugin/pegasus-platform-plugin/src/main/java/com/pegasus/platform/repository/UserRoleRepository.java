package com.pegasus.platform.repository;

import com.pegasus.platform.domain.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @Author: enHui.Chen
 * @Description:
 * @Data 2019/11/8
 */
public interface UserRoleRepository extends JpaRepository<UserRole, Long>, JpaSpecificationExecutor {
    List<UserRole> findByUserId(Long userId);

    @Modifying
    @Query(
            nativeQuery = true,
            value = "DELETE FROM pe_user_role where user_id = :userId AND role_id =:roleId"
    )
    int deleteDispatchRole(Long userId, Long roleId);
}
