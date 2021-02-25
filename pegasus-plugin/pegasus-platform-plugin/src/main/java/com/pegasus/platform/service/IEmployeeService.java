package com.pegasus.platform.service;

import com.pegasus.common.pagehelper.custom.PageResponse;
import com.pegasus.common.service.ICommonService;
import com.pegasus.platform.domain.Employee;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by enHui.Chen on 2019/9/6.
 */
public interface IEmployeeService<T> extends ICommonService<T> {
    Employee findByUserId(Long userId);

    Employee findManagerByUserId(Long userId);

    List<Employee> findProcessTestEmployees();

    PageResponse<Employee> findEmployeeByPage(Pageable pageable);

    List<Employee> findByFullNameLike(String fullName);
}
