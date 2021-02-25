package com.pegasus.activiti.custom;

import com.pegasus.activiti.constant.ActivitiConstants;
import com.pegasus.activiti.domain.ApproverRule;
import com.pegasus.activiti.service.IApproveRuleService;
import com.pegasus.activiti.utils.ActivitiUtil;
import org.activiti.bpmn.model.FormProperty;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.el.ExpressionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by enHui.Chen on 2019/9/23.
 */
@Component
public class ApproverHandler {
    @Autowired
    private ProcessEngineConfigurationImpl processEngineConfiguration;
    @Autowired
    private IApproveRuleService approveRuleService;

    /**
     * @Author: enHui.Chen
     * @Description: 获取审批人
     * @Data 2019/9/23
     */
    public Set<String> getApprovers(DelegateExecution execution) {
        UserTask userTask = (UserTask) execution.getCurrentFlowElement();
        Assert.notNull(userTask, "获取审批人失败！");
        return process(userTask, execution);
    }

    private Set<String> process(UserTask userTask, DelegateExecution execution) {
        Set<String> approvers = new LinkedHashSet<>();
        // 获取指定表单属性
        List<FormProperty> formProperties = userTask.getFormProperties();
        FormProperty approver = ActivitiUtil.getFormPropertyById(formProperties, ActivitiConstants.APPROVE_CANDIDATE_APPROVER);
        Assert.notNull(approver, "请至少设置一个候选审批人");
        // 通过ExpressionManager进行转换
        ExpressionManager expressionManager = processEngineConfiguration.getExpressionManager();
        approver.getFormValues().forEach(formValue -> {
            // 获取对应的审批规则表达式
            ApproverRule approverRule = approveRuleService.findApproverRuleByRuleCode(formValue.getId());
            Assert.notNull(approverRule, "无法获取对应的审批规则,请检查规则设计!");
            // 使用当前规则不指定人员
            Object value = expressionManager.createExpression(approverRule.getRuleEl()).getValue(execution);
            if (value == null) {
                return;
            }
            Set<String> partOfApprover = new LinkedHashSet<>();
            if (value instanceof String) {
                partOfApprover.add((String) value);
            } else if (value instanceof Long) {
                partOfApprover.add(String.valueOf(value));
            } else if (value instanceof Set) {
                partOfApprover.addAll((Set<String>) value);
            } else if (value instanceof List) {
                partOfApprover.addAll((List<String>) value);
            }
            approvers.addAll(partOfApprover);
            // 指定人员/指定岗位/指定角色
        });
        return approvers;
    }
}
