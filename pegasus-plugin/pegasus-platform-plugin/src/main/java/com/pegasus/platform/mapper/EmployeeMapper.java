package com.pegasus.platform.mapper;

import com.pegasus.platform.domain.Employee;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * Created by enHui.Chen on 2019/11/8.
 */
public interface EmployeeMapper extends Mapper<Employee> {

    @Select("SELECT " +
            "pe.* " +
            "FROM " +
            "pe_user pu, " +
            "pe_employee pe " +
            "WHERE " +
            "pu.id = pe.user_id " +
            "AND pu.is_active = 1 " +
            "AND pu.user_type = 'user' ")
    List<Employee> findProcessTestEmployees();
}
