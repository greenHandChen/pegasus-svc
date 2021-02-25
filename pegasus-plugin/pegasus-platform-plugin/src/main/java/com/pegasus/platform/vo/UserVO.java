package com.pegasus.platform.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pegasus.platform.domain.User;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Created by enHui.Chen on 2019/9/6.
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserVO extends User {
    private String loginName;
    private String email;
    private String phone;
    private Long roleId;
    private List<Long> roleIds;
    private String employeeCode;
    private String fullName;
    private String title;
    private String address;
    private Boolean isActive;
    private String sex;
    private Long age;
}
