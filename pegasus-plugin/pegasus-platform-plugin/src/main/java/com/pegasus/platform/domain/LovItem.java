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
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "pe_lov_item")
public class LovItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private Long lovId;// lovId
    @Column
    private String description;// lovItem描述
    @Column
    private Boolean isConditionField;// 是否是查询条件
    @Column
    private Integer conditionFieldWidth;// 查询字段宽度
    @Column
    private String conditionFieldName;// 查询字段名称
    @Column
    private String conditionFieldLovCode;// 查询字段编码
    @Column
    private Integer conditionFieldSequence;// 查询字段排序
    @Column
    private String conditionFieldValue;// 查询字段的value
    @Column
    private String conditionFieldText;// 查询字段的text
    @Column
    private String conditionFieldTextfield;//
    @Column
    private Boolean isColumnField;// 是否列表字段
    @Column
    private String columnFieldName;// 列表字段名称
    @Column
    private String columnFieldCode;// 列表字段编码
    @Column
    private Integer columnFieldSequence;// 列表字段序号
    @Column
    private Integer columnFieldWidth;// 列表字段宽度
    @Column
    private String columnFieldAlign;// 列表字段布局
}
