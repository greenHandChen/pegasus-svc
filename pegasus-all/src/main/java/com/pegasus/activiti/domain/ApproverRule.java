package com.pegasus.activiti.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;

/**
 * Created by enHui.Chen on 2019/9/23.
 */
@Data
@Entity
@Table(name = "pe_approver_rule")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApproverRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String ruleName;
    @Column
    private String ruleEl;
    @Column
    private String ruleCode;
    @Column
    private Boolean isEnabled;
}
