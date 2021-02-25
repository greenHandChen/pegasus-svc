package com.pegasus.activiti.custom.listener;

import com.pegasus.activiti.constant.ActivitiConstants;
import com.pegasus.common.utils.ApplicationContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Created by enHui.Chen on 2019/11/5.
 */
@Slf4j
@Component
public class UserTaskCompleteListener implements TaskListener {

    @Override
    public void notify(DelegateTask delegateTask) {
//        ExecutionEntity execution = Context.getCommandContext().getExecutionEntityManager().findById(delegateTask.getExecutionId());
//        String outcome = (String) execution.getVariable(ActivitiConstants.APPROVE_MARK);
//        DelegateExecution rootExecution = getMultiInstanceRootExecution(execution);
//        if (ActivitiConstants.APPROVED.equals(outcome)) {
//            setLoopVariable(rootExecution, ActivitiConstants.NUMBER_OF_APPROVED, getLoopVariable(rootExecution, ActivitiConstants.NUMBER_OF_APPROVED) + 1);
//        } else {
//            setLoopVariable(rootExecution, ActivitiConstants.NUMBER_OF_REJECTED, getLoopVariable(rootExecution, ActivitiConstants.NUMBER_OF_REJECTED) + 1);
//        }
//        // 1.判断任务是否已结束
//        Integer approvedInstances = getLoopVariable(rootExecution, ActivitiConstants.NUMBER_OF_APPROVED);
//        Integer rejectedInstances = getLoopVariable(rootExecution, ActivitiConstants.NUMBER_OF_REJECTED);
//        Integer nrOfInstances = getLoopVariable(rootExecution, "nrOfInstances");
//        Integer nrOfCompletedInstances = getLoopVariable(rootExecution, "nrOfCompletedInstances");
//        if (nrOfInstances.compareTo(nrOfCompletedInstances) == 0) {
//            ApplicationContext applicationContext = ApplicationContextUtil.getApplicationContext();
//            RuntimeService runtimeService = applicationContext.getBean("runtimeService", RuntimeService.class);
//            // 2.查询审批方式(一票通过/一票否决/按比例)
//            // 一票否决
//            if (rejectedInstances.compareTo(0) > 0) {
//                runtimeService.setVariable(execution.getProcessInstanceId(), ActivitiConstants.APPROVE_MARK, ActivitiConstants.REJECTED);
//            } else {
//                runtimeService.setVariable(execution.getProcessInstanceId(), ActivitiConstants.APPROVE_MARK, ActivitiConstants.APPROVED);
//
//            }
//        }
    }
}
