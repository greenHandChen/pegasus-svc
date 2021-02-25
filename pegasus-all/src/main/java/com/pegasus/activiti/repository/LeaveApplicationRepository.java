package com.pegasus.activiti.repository;

import com.pegasus.activiti.domain.LeaveApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by enHui.Chen on 2019/9/17.
 */
public interface LeaveApplicationRepository extends JpaRepository<LeaveApplication, Long>, JpaSpecificationExecutor {
}
