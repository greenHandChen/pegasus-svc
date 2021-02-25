package com.pegasus.activiti.service;

import com.pegasus.activiti.vo.ProcessInstanceVO;
import com.pegasus.activiti.vo.ProcessNodeVO;
import com.pegasus.activiti.vo.TaskVO;
import com.pegasus.common.pagehelper.custom.PageResponse;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * Created by enHui.Chen on 2019/8/29.
 */
public interface IActivitiService {
    List<Model> findProcessDefinition(String name);

    List<ProcessDefinition> templateProcessDefinition();

    String createProcessDefinition(Map<String, String> maps) throws UnsupportedEncodingException;

    ResponseEntity deployProcessDefinition(String modelId) throws IOException;

    void startProcessTest(String userId, Long amount, String processDefinitionId);

    List<TaskVO> findTaskCurrentAssignee();

    List<TaskVO> findTaskAdminAssignee();

    void completeTask(String taskId, String outcome);

    void jumpTask(String taskId, String destinationActivitiId);

    PageResponse<ProcessInstanceVO> findProcessInstanceMonitor(Pageable pageRequest);

    List<ProcessNodeVO> findProcessJumpNode(String processDefinitionId);

    List<TaskVO> findDeliverTask(String processInstanceId);

    void deleteTask(String processInstanceId);

    void suspendOrActiveTask(String processInstanceId);

    void batchDeliverTask(Map<String, String> deliverMap);

    void counterSignAddTask(String taskId, String assignee);

    void counterSignReduceTask(String taskId);

    List<TaskVO> findCounterSignAddOrReduceTask(String taskDefKey, String processInstanceId);
}
