package com.pegasus.activiti.service;

import com.pegasus.activiti.domain.LeaveApplication;

/**
 * Created by enHui.Chen on 2019/9/17.
 */
public interface ILeaveApplicationService {
    void insertSelective(LeaveApplication leaveApplication);

    void updateStatusById(LeaveApplication leaveApplication);
}
