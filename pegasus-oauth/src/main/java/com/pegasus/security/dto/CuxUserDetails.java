package com.pegasus.security.dto;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.List;

/**
 * Created by enHui.Chen on 2019/9/5.
 */
@Data
public class CuxUserDetails extends User {
    private static final long serialVersionUID = 1L;

    private Long userId;
    private String phone;
    private Boolean isActive;
    private String email;
    private Long roleId;
    private List<Long> roleIds;

    public CuxUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public CuxUserDetails(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }
}
