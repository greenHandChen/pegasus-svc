package com.pegasus.platform.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Created by enHui.Chen on 2019/9/4.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "pe_role")
public class Role {
    public Role(Long id, String roleCode, String roleName, Long parentId, String parentLevelPath, Long extendId,
                String extendLevelPath, Boolean isActive, String parentRoleName, String extendRoleName) {
        this.id = id;
        this.roleCode = roleCode;
        this.roleName = roleName;
        this.parentId = parentId;
        this.parentLevelPath = parentLevelPath;
        this.extendId = extendId;
        this.extendLevelPath = extendLevelPath;
        this.isActive = isActive;
        this.parentRoleName = parentRoleName;
        this.extendRoleName = extendRoleName;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String roleCode;
    @Column
    private String roleName;
    @Column
    private Long parentId;
    @Column
    private String parentLevelPath;
    @Column
    private Long extendId;
    @Column
    private String extendLevelPath;
    @Column
    private Boolean isActive;

    @Transient
    private String parentRoleName;
    @Transient
    private String extendRoleName;
    @Transient
    private String action;

}
