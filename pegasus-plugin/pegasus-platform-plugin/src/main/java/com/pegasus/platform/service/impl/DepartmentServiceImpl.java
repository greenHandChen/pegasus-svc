package com.pegasus.platform.service.impl;

import com.pegasus.platform.domain.Department;
import com.pegasus.platform.repository.DepartmentRepository;
import com.pegasus.platform.service.IDepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

//import com.pegasus.platform.repository.DepartmentRepository;

/**
 * Created by enHui.Chen on 2019/10/22.
 */
@Service
public class DepartmentServiceImpl implements IDepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public Department findDepartmentByUserId(Long userId) {
        return departmentRepository.findDepartmentByUserId(userId);
    }

    @Override
    public Department findDepartmentByDepartmentId(Long departmentId) {
        Optional<Department> department = departmentRepository.findById(departmentId);
        // 有值返回,无值返回null
        return department.orElse(null);
    }
}
