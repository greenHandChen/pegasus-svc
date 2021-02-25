package com.pegasus.activiti.repository;

import com.pegasus.activiti.vo.TaskVO;
import com.pegasus.common.utils.JpaQueryUtil;
import com.pegasus.security.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by enHui.Chen on 2019/9/18.
 */
@Repository
public class TaskDao {
    private static final String TASK_CURRENT_ASSIGNEE = "SELECT " +
            " art.id_ id, " +
            " art.PROC_INST_ID_ processInstanceId, " +
            " arp.NAME_ processName, " +
            " art.NAME_ name, " +
            " DATE_FORMAT(art.CREATE_TIME_,'%Y-%m-%d %H:%i:%s') createdTime, " +
            " art.PRIORITY_ priority, " +
            "(select CONCAT(pe.full_name,\"(\",pe.employee_code,\")\") from pe_employee pe where pe.user_id = are.START_USER_ID_)startUserName, " +
            "(select CONCAT(pe.full_name,\"(\",pe.employee_code,\")\") from pe_employee pe where pe.user_id = art.ASSIGNEE_)assigneeName " +
            "FROM " +
            " act_ru_task art, " +
            " ACT_RU_EXECUTION are, " +
            " act_re_procdef arp " +
            "WHERE arp.ID_ = art.PROC_DEF_ID_ " +
            "and art.PROC_INST_ID_ = are.PROC_INST_ID_ " +
            "and are.IS_SCOPE_ = 1 ";

    @Autowired
    private EntityManager entityManager;

    public List<TaskVO> findTaskCurrentAssignee() {
        StringBuilder sb = new StringBuilder(TASK_CURRENT_ASSIGNEE);
        sb.append(" and art.ASSIGNEE_ = :userId ");

        Query query = entityManager.createNativeQuery(sb.toString(), TaskVO.class);
        query.setParameter("userId", SecurityUtil.getCurrentUser().getUserId());

        return JpaQueryUtil.getResultList(query, TaskVO.class);
    }

    public List<TaskVO> findTaskAdminAssignee() {
        Query query = entityManager.createNativeQuery(TASK_CURRENT_ASSIGNEE);
        return JpaQueryUtil.getResultList(query, TaskVO.class);
    }

    public List<TaskVO> findMonitorTaskByProcessInstanceIds(List<String> processInstanceIds) {
        StringBuilder sb = new StringBuilder(TASK_CURRENT_ASSIGNEE);
        sb.append(" and art.PROC_INST_ID_ in (:processInstanceIds) ");

        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("processInstanceIds", processInstanceIds);
        return JpaQueryUtil.getResultList(query, TaskVO.class);
    }
}
