package com.pegasus.activiti.mapper;

import com.pegasus.activiti.vo.TaskVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by enHui.Chen on 2019/11/14.
 */
public interface TaskMapper {
    @Select(" SELECT " +
            "  art.ID_ id, " +
            "  art.name_ name, " +
            "  art.assignee_ assignee, " +
            " (select CONCAT(pe.full_name,'(',pe.employee_code,')') from pe_employee pe where pe.user_id = art.assignee_ )assigneeName " +
            " FROM " +
            "  act_ru_task art " +
            " WHERE " +
            "   art.PROC_INST_ID_ = #{processInstanceId}  " +
            " AND art.TASK_DEF_KEY_ = #{taskDefKey} ")
    List<TaskVO> findCounterSignAddOrReduceTask(@Param("taskDefKey") String taskDefKey, @Param("processInstanceId") String processInstanceId);
}
