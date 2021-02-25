package com.pegasus.platform.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by enHui.Chen on 2019/9/6.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "pe_employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private Long userId;
    @Column
    private String employeeCode;
    @Column
    private String fullName;
    @Column
    private String title;
    @Column
    private String email;
    @Column
    private String address;
    @Column
    private String sex;
    @Column
    private Date birthday;

    @Transient
    private String showName;
}
