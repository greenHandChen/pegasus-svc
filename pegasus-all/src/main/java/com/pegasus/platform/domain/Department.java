package com.pegasus.platform.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Created by enHui.Chen on 2019/9/19.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "pe_department")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String departmentName;// 部门名称
    @Column
    private String path;// 部门路径
     @Column
    private String levelPath;// 部门路径ID
    @Column
    private String departmentCode;// 部门编码
    @Column
    private Long parentId;// 上级部门ID
    @Column
    private Long managerId;// 部门经理员工ID
}
