package com.pegasus.activiti.repository;

import com.pegasus.activiti.domain.ApproverRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by enHui.Chen on 2019/9/23.
 */
public interface ApproveRuleRepository extends JpaRepository<ApproverRule, Long> {
    ApproverRule findApproverRuleByRuleCode(String ruleCode);

    List<ApproverRule> findApproverRuleByIsEnabled(boolean enabled);
}
