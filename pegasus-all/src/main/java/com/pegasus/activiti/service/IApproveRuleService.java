package com.pegasus.activiti.service;

import com.pegasus.activiti.domain.ApproverRule;

import java.util.List;

/**
 * Created by enHui.Chen on 2019/9/23.
 */
public interface IApproveRuleService {
    ApproverRule findApproverRuleByRuleCode(String ruleCode);

    List<ApproverRule> findApproverRuleByIsEnabled(boolean enabled);
}
