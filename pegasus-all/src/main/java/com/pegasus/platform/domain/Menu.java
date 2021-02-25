package com.pegasus.platform.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pegasus.common.dto.CommonDTO;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * Created by enHui.Chen on 2019/8/30.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "pe_menu")
public class Menu extends CommonDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private Integer sort;
    @Column
    private String code;
    @Column
    private String name;
    @Column
    private String path;
    @Column
    private String type;
    @Column
    private String levelPath;
    @Column
    private Long parentId;
    @Column
    private Boolean isLeaf;
    @Column
    private String leftClass;
    @Column
    private Boolean isActive;
    @Transient
    private String parentLevelPath;
    @Transient
    private List<Menu> routerData;// 菜单Tree
}
