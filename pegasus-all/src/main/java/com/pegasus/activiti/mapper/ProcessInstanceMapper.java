package com.pegasus.activiti.mapper;

import com.pegasus.activiti.vo.ProcessInstanceVO;
import com.pegasus.activiti.vo.TaskVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**a
 * Created by enHui.Chen on 2019/11/11.
 */
public interface ProcessInstanceMapper {
    @Select("select t.* from ( " +
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
            "  and art.ID_ is null)) t ")
    List<ProcessInstanceVO> findProcessInstanceMonitor();
}
