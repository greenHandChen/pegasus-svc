package com.pegasus.activiti.delegate;

import com.pegasus.activiti.constant.ActivitiConstants;
import com.pegasus.activiti.domain.LeaveApplication;
import com.pegasus.activiti.service.ILeaveApplicationService;
import com.pegasus.common.exception.CommonException;
import com.pegasus.common.utils.ApplicationContextUtil;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

/**
 * Created by enHui.Chen on 2019/10/23.
 */
public class LeaveApplicationApprovalDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {
        Object outcome = execution.getVariable("outcome");
        String processInstanceBusinessKey = execution.getProcessInstanceBusinessKey();
        if (ActivitiConstants.OUTCOME_LIST.contains(outcome)) {
            Long leaveApplcationId = Long.valueOf(processInstanceBusinessKey.split(ActivitiConstants.SPLIT_CHAR)[1]);
            ILeaveApplicationService ila = ApplicationContextUtil.getApplicationContext().getBean("leaveApplicationServiceImpl", ILeaveApplicationService.class);
            LeaveApplication leaveApplication = LeaveApplication.builder()
                    .id(leaveApplcationId)
                    .status((String) outcome)
                    .build();
            ila.updateStatusById(leaveApplication);
            return;
        }
        throw new CommonException("审批结束时产生的未知错误:{}", processInstanceBusinessKey);

    }
}
