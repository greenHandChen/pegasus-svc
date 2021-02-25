package com.pegasus.activiti.listener;

import com.pegasus.platform.domain.Employee;
import com.pegasus.platform.service.IEmployeeService;
import com.pegasus.security.utils.SecurityUtil;
import com.pegasus.common.utils.ApplicationContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;

/**
 * Created by enHui.Chen on 2019/9/19.
 */
@Slf4j
public class ManagerTaskListener implements TaskListener {

    /**
     * @Author: enHui.Chen
     * @Description: 部门主管
     * @Data 2019/9/19
     */
    @Override
    public void notify(DelegateTask delegateTask) {
        String assignee = delegateTask.getAssignee();
    }
}
