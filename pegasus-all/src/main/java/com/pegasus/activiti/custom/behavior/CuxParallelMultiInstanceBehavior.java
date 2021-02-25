package com.pegasus.activiti.custom.behavior;

import com.pegasus.activiti.constant.ActivitiConstants;
import com.pegasus.activiti.custom.ApproverHandler;
import com.pegasus.activiti.utils.ActivitiUtil;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.Activity;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.impl.bpmn.behavior.AbstractBpmnActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.ParallelMultiInstanceBehavior;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by enHui.Chen on 2019/9/23.
 */
@Slf4j
public class CuxParallelMultiInstanceBehavior extends ParallelMultiInstanceBehavior {
    @Autowired
    private ApproverHandler approverHandler;

    public CuxParallelMultiInstanceBehavior(Activity activity, AbstractBpmnActivityBehavior originalActivityBehavior) {
        super(activity, originalActivityBehavior);
    }

    /**
     * @Author: enHui.Chen
     * @Description: 创建UserTask多实例流程节点(动态设置候选人)
     * @Data 2019/9/23
     */
    @Override
    protected int createInstances(DelegateExecution execution) {
        if (execution.getCurrentFlowElement() instanceof UserTask) {
            super.setLoopVariable(execution, ActivitiConstants.ASSIGNEE_LIST, approverHandler.getApprovers(execution));
        }

        int instances = super.createInstances(execution);
        // 设置通过/拒绝初始值为0
        super.setLoopVariable(execution, ActivitiConstants.NUMBER_OF_APPROVED, 0);
        super.setLoopVariable(execution, ActivitiConstants.NUMBER_OF_REJECTED, 0);
        return instances;
    }

    /**
     * @Author: enHui.Chen
     * @Description: userTask多实例结束之后，于complete事件之后执行
     * @Data 2019/11/5
     */
    @Override
    public void leave(DelegateExecution execution) {
        String outcome = (String) execution.getVariable(ActivitiConstants.APPROVE_MARK);
        DelegateExecution rootExecution = getMultiInstanceRootExecution(execution);
        if (ActivitiConstants.APPROVED.equals(outcome)) {
            setLoopVariable(rootExecution, ActivitiConstants.NUMBER_OF_APPROVED, getLoopVariable(rootExecution, ActivitiConstants.NUMBER_OF_APPROVED) + 1);
        } else {
            setLoopVariable(rootExecution, ActivitiConstants.NUMBER_OF_REJECTED, getLoopVariable(rootExecution, ActivitiConstants.NUMBER_OF_REJECTED) + 1);
        }
        super.leave(execution);
    }

}
