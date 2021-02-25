package com.pegasus.platform.service.impl;


import com.pegasus.common.pagehelper.custom.PageResponse;
import com.pegasus.common.pagehelper.custom.utils.PageHelperUtil;
import com.pegasus.common.service.impl.CommonServiceImpl;
import com.pegasus.platform.domain.Employee;
import com.pegasus.platform.mapper.EmployeeMapper;
import com.pegasus.platform.repository.EmployeeRepository;
import com.pegasus.platform.service.IEmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Created by enHui.Chen on 2019/9/6.
 */
@Slf4j
@Service
public class EmployeeServiceImpl extends CommonServiceImpl<Employee> implements IEmployeeService<Employee> {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public Employee findByUserId(Long userId) {
        Employee employee = new Employee();
        employee.setId(userId);
        return employeeMapper.selectByPrimaryKey(userId);
    }

    @Override
    public Employee findManagerByUserId(Long userId) {
        return employeeRepository.findManagerByUserId(userId);
    }

    @Override
    public List<Employee> findProcessTestEmployees() {
        return employeeMapper.findProcessTestEmployees();
    }

    @Override
    public PageResponse<Employee> findEmployeeByPage(Pageable pageable) {
        return PageHelperUtil.doPageQuery(pageable, () -> {
            return employeeMapper.selectAll();
        });
    }

    @Override
    public List<Employee> findByFullNameLike(String fullName) {
        List<Employee> employees = employeeRepository.findByFullNameLike(fullName);
        if (CollectionUtils.isEmpty(employees)) {
            return employees;
        }
        employees.forEach(employee -> employee.setShowName(employee.getFullName() + "(" + employee.getEmployeeCode() + ")"));
        return employees;
    }
}
