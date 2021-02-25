package com.pegasus.platform.controller;

import com.pegasus.common.pagehelper.custom.PageResponse;
import com.pegasus.platform.domain.Employee;
import com.pegasus.platform.service.IEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by enHui.Chen on 2019/10/31.
 */
@RestController
@RequestMapping("/v1/employee")
public class EmployeeController {
    @Autowired
    private IEmployeeService employeeService;

    /**
     * @Author: enHui.Chen
     * @Description: 根据员工姓名模糊搜索员工信息
     * @Data 2019/10/31
     */
    @GetMapping("/find/employeeByName/{value}")
    public ResponseEntity<List<Employee>> findEmployeeByName(@PathVariable(name = "value") String value) {
        List<Employee> employees = employeeService.findByFullNameLike(value);
        return ResponseEntity.ok(employees);
    }

    /**
     * @Author: enHui.Chen
     * @Description: 获取员工信息
     * @Data 2019/9/16
     */
    @GetMapping("/find/employeeByPage")
    public ResponseEntity<PageResponse> findEmployeeByPage(Pageable pageable) {
        return new ResponseEntity<>(employeeService.findEmployeeByPage(pageable), HttpStatus.OK);
    }
}
