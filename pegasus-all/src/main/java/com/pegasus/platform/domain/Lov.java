package com.pegasus.platform.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Created by enHui.Chen on 2019/11/11.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "pe_lov")
public class Lov {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String lovCode;// lov编码
    @Column
    private String description;// lov描述
    @Column
    private String sqlId;// mybatis的sql的id
    @Column
    private String valueField;// 传值
    @Column
    private String textField;// 展示
    @Column
    private String title;//
    @Column
    private Integer width;//
    @Column
    private Integer height;//
    @Column
    private String placeholder;// 提示
    @Column
    private String customSql;// 自定义sql
    @Column
    private String customUrl;// 自定义url
    @Column
    private String lov_page_size;// 每页页数
}
