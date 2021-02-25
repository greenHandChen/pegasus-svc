package com.pegasus.activiti.custom.behavior;

import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;

/**
 * Created by enHui.Chen on 2019/9/23.
 */
public class CuxUserTaskActivityBehavior extends UserTaskActivityBehavior {
    public CuxUserTaskActivityBehavior(UserTask userTask) {
        super(userTask);
    }

    /**
     * @Author: enHui.Chen
     * @Description: 创建UserTask流程节点(动态设置候选人)
     * @Data 2019/9/23
     */
    @Override
    public void execute(DelegateExecution execution) {
        super.execute(execution);
    }
}
