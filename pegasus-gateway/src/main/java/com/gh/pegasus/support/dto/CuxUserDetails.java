package com.gh.pegasus.support.dto;

import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * Created by enHui.Chen on 2019/9/5.
 */
@Data
public class CuxUserDetails {
    private Long userId;
    private String phone;
    private Boolean isActive;
    private String email;
    private Long roleId;
    private List<Long> roleIds;

    private String password;
    private String username;
    private Boolean accountNonExpired;
    private Boolean accountNonLocked;
    private Boolean credentialsNonExpired;
    private Boolean enabled;

    private Boolean clientOnly;
    private String clientId;
    private String grantType;
    private Set<String> scope;
    private Set<String> resourceIds;
    private Boolean refresh;
    private Boolean approved;
    private String redirectUri;

}
