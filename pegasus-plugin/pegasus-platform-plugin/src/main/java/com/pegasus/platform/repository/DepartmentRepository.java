package com.pegasus.platform.repository;

import com.pegasus.platform.domain.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @Author: enHui.Chen
 * @Description:
 * @Data 2019/11/8
 */
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    @Query(nativeQuery = true,
            value = "SELECT " +
                    " pd.* " +
                    "FROM " +
                    " pe_department pd, " +
                    " pe_employee pe, " +
                    " pe_employee_department ped " +
                    "WHERE " +
                    " ped.employee_id = pe.id " +
                    "AND ped.department_id = pd.id " +
                    "AND pe.user_id = :userId ")
    Department findDepartmentByUserId(Long userId);
}
