package com.pegasus.activiti.service.impl;

import com.pegasus.activiti.domain.LeaveApplication;
import com.pegasus.activiti.repository.LeaveApplicationRepository;
import com.pegasus.activiti.service.ILeaveApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Created by enHui.Chen on 2019/9/17.
 */
@Service
public class LeaveApplicationServiceImpl implements ILeaveApplicationService {
    @Autowired
    private LeaveApplicationRepository leaveApplicationRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertSelective(LeaveApplication leaveApplication) {
        leaveApplicationRepository.save(leaveApplication);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatusById(LeaveApplication leaveApplication) {
        leaveApplicationRepository.findById(leaveApplication.getId()).ifPresent(la -> {
            la.setStatus(leaveApplication.getStatus());
            leaveApplicationRepository.save(la);
        });
    }
}
