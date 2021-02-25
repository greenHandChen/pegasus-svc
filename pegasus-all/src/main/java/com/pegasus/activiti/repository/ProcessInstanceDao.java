package com.pegasus.activiti.repository;

import com.pegasus.activiti.vo.ProcessInstanceVO;
import com.pegasus.common.utils.JpaQueryUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by enHui.Chen on 2019/10/29.
 */
@Repository
public class ProcessInstanceDao {
    private static final String PROCESS_MONITOR_SQL_PREFIX = "select t.* from ( " +
            "(SELECT     " +
            "  art.TASK_DEF_KEY_ taskDefKey, " +
            "  art.id_ taskId, " +
            "  ahp.PROC_DEF_ID_ procDefId,  " +
            "  ahp.id_ id,     " +
            "  arp.NAME_ processName,     " +
            "  DATE_FORMAT(ahp.START_TIME_,'%Y-%m-%d %H:%i:%s') startTime,     " +
            "  DATE_FORMAT(ahp.END_TIME_,'%Y-%m-%d %H:%i:%s') endTime,     " +
            "  (select CONCAT(pe.full_name,'(',pe.employee_code,')') from pe_employee pe where pe.user_id = ahp.START_USER_ID_)startUserName,    " +
            "  are.SUSPENSION_STATE_ suspensionState, " +
            "  GROUP_CONCAT((select CONCAT(pe.full_name,'(',pe.employee_code,')') from pe_employee pe where pe.user_id = art.ASSIGNEE_),',') assignees,  " +
            "  art.NAME_ name " +
            " FROM   " +
            "  act_re_procdef arp,   " +
            "  act_hi_procinst ahp   " +
            "  left join act_ru_execution are on are.PROC_INST_ID_ = ahp.ID_ and are.IS_SCOPE_ = 1  " +
            "  left join act_ru_task art on art.PROC_INST_ID_ = ahp.ID_ " +
            " where ahp.PROC_DEF_ID_ = arp.ID_ " +
            "   and art.ID_ is not null " +
            " group by art.PROC_INST_ID_,art.TASK_DEF_KEY_) " +
            " union all " +
            "(SELECT " +
            "  null taskDefKey, " +
            "  null taskId, " +
            "  ahp.PROC_DEF_ID_ procDefId,  " +
            "  ahp.id_ id,     " +
            "  arp.NAME_ processName,     " +
            "  DATE_FORMAT(ahp.START_TIME_,'%Y-%m-%d %H:%i:%s') startTime,     " +
            "  DATE_FORMAT(ahp.END_TIME_,'%Y-%m-%d %H:%i:%s') endTime,     " +
            "  (select CONCAT(pe.full_name,'(',pe.employee_code,')') from pe_employee pe where pe.user_id = ahp.START_USER_ID_)startUserName,     " +
            "  are.SUSPENSION_STATE_ suspensionState, " +
            "  null assignees, " +
            "  null name " +
            " FROM   " +
            "  act_re_procdef arp,   " +
            "  act_hi_procinst ahp   " +
            "  left join act_ru_execution are on are.PROC_INST_ID_ = ahp.ID_ and are.IS_SCOPE_ = 1  " +
            "  left join act_ru_task art on art.PROC_INST_ID_ = ahp.ID_ " +
            " where ahp.PROC_DEF_ID_ = arp.ID_ " +
            "  and art.ID_ is null)) t ";

    @Autowired
    private EntityManager entityManager;

    public Page<ProcessInstanceVO> findProcessInstanceMonitor(Pageable pageRequest) {
        StringBuilder sb = new StringBuilder();
        sb.append(PROCESS_MONITOR_SQL_PREFIX);
        Query query = entityManager.createNativeQuery(sb.toString());
        return JpaQueryUtil.getResultListByPage(query, pageRequest, ProcessInstanceVO.class);
    }

    public List<ProcessInstanceVO> findProcessInstanceMonitorByProcessInstanceId(String processInstanceId) {
        StringBuilder sb = new StringBuilder();
        sb.append(PROCESS_MONITOR_SQL_PREFIX)
                .append(" where t.id = :processInstanceId ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("processInstanceId", processInstanceId);
        return JpaQueryUtil.getResultList(query, ProcessInstanceVO.class);
    }

}
