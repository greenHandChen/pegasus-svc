package com.pegasus.platform.repository;

import com.pegasus.platform.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @Author: enHui.Chen
 * @Description:
 * @Data 2019/11/8
 */
public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor {
    @Query(nativeQuery = true,
            value = "SELECT " +
                    " pe.* " +
                    "FROM " +
                    " pe_employee pe " +
                    "WHERE " +
                    " pe.full_name LIKE concat(concat('%' ,:fullName),'%') "
    )
    List<Employee> findByFullNameLike(String fullName);

    @Query(nativeQuery = true,
            value = "SELECT " +
                    "pe.* " +
                    "FROM " +
                    "pe_user pu, " +
                    "pe_employee pe " +
                    "WHERE " +
                    "pu.id = pe.user_id " +
                    "AND pu.is_active = 1 " +
                    "AND pu.user_type = 'user' ")
    List<Employee> findProcessTestEmployees();

    @Query(nativeQuery = true,
            value = "SELECT " +
                    " pem.* " +
                    "FROM " +
                    " pe_employee pe, " +
                    " pe_employee_department ped, " +
                    " pe_department pd, " +
                    " pe_employee pem " +
                    "WHERE " +
                    " pe.id = ped.employee_id " +
                    "AND ped.department_id = pd.id " +
                    "and pem.id = pd.manager_id " +
                    "and pe.user_id = :userId")
    Employee findManagerByUserId(Long userId);
}
