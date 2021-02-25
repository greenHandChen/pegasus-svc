package com.pegasus.activiti.service.impl;

import com.pegasus.activiti.service.IActivitiEmpService;
import com.pegasus.common.utils.ApplicationContextUtil;
import com.pegasus.platform.domain.Department;
import com.pegasus.platform.domain.Employee;
import com.pegasus.platform.repository.EmployeeRepository;
import com.pegasus.platform.service.IDepartmentService;

import java.io.Serializable;
import java.util.Optional;


/**
 * Created by enHui.Chen on 2019/9/20.
 */
public class ActivitiEmpServiceImpl implements IActivitiEmpService, Serializable {

    @Override
    public Long findManager(String userId) {
        EmployeeRepository employeeRepository = ApplicationContextUtil.getApplicationContext().getBean("employeeRepository", EmployeeRepository.class);
        Employee managerByUserId = employeeRepository.findManagerByUserId(Long.valueOf(userId));
        return managerByUserId.getUserId();
    }

    @Override
    public Long findManagerByLevel(String userId, int level) {
        IDepartmentService departmentService = ApplicationContextUtil.getApplicationContext().getBean("departmentServiceImpl", IDepartmentService.class);
        Department department = departmentService.findDepartmentByUserId(Long.valueOf(userId));
        String[] levelPath = department.getLevelPath().split("/");

        EmployeeRepository employeeRepository = ApplicationContextUtil.getApplicationContext().getBean("employeeRepository", EmployeeRepository.class);
        Long departmentId = Long.valueOf(levelPath[level]);
        Department departmentByDepartmentId = departmentService.findDepartmentByDepartmentId(departmentId);
        Optional<Employee> employee = employeeRepository.findById(departmentByDepartmentId.getManagerId());
        return employee.get().getUserId();
    }
}
