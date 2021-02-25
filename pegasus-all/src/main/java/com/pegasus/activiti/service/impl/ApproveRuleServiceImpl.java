package com.pegasus.activiti.service.impl;

import com.pegasus.activiti.domain.ApproverRule;
import com.pegasus.activiti.repository.ApproveRuleRepository;
import com.pegasus.activiti.service.IApproveRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by enHui.Chen on 2019/9/23.
 */
@Service
public class ApproveRuleServiceImpl implements IApproveRuleService {
    @Autowired
    private ApproveRuleRepository approveRuleRepository;

    public ApproverRule findApproverRuleByRuleCode(String ruleCode) {
        return approveRuleRepository.findApproverRuleByRuleCode(ruleCode);
    }

    public List<ApproverRule> findApproverRuleByIsEnabled(boolean enabled) {
        return approveRuleRepository.findApproverRuleByIsEnabled(enabled);
    }
}
