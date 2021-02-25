package com.pegasus.platform.service;

import com.pegasus.platform.domain.Department;

/**
 * Created by enHui.Chen on 2019/10/22.
 */
public interface IDepartmentService {
    Department findDepartmentByUserId(Long userId);

    Department findDepartmentByDepartmentId(Long userId);
}
