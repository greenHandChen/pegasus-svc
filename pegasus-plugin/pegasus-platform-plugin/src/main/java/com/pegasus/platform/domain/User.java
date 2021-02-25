package com.pegasus.platform.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;

/**
 * Created by enHui.Chen on 2019/8/30.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "pe_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String userType;
    @Column
    private String username;
    @Column
    private String password;
    @Column
    private String nickName;
    @Column
    private String email;
    @Column
    private String phone;
    @Column
    private Boolean isActive;
}
