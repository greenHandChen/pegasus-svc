package com.pegasus.activiti.custom.cmd;

import com.pegasus.activiti.utils.ActivitiUtil;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ExecutionEntityManager;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntityManager;
import org.activiti.engine.impl.util.ProcessDefinitionUtil;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: enHui.Chen
 * @Description: 节点加签(多实例) 创建task任务以及执行流即可,loopCounter与nrOfInstances增1
 * @Data 2019/11/6
 */
public class MultiNodeCounterSignAddCommand implements Command<Object> {
    private String taskId;// 任务Id
    private String assignee;// 加签人

    public MultiNodeCounterSignAddCommand(String taskId, String assignee) {
        this.taskId = taskId;
        this.assignee = assignee;
    }

    @Override
    public Object execute(CommandContext commandContext) {
        TaskEntityManager taskEntityManager = commandContext.getTaskEntityManager();
        ExecutionEntityManager executionEntityManager = commandContext.getExecutionEntityManager();
        // 获取当前task的父执行流(多实例一共有三级执行流,此处获取的是第二级)
        TaskEntity taskEntity = taskEntityManager.findById(taskId);
        ExecutionEntity thirdExecutionEntity = executionEntityManager.findById(taskEntity.getExecutionId());
        ExecutionEntity secondExecutionEntity = thirdExecutionEntity.getParent();

        // 创建加签的执行流
        Process process = ProcessDefinitionUtil.getProcess(taskEntity.getProcessDefinitionId());
        ExecutionEntity counterSignExecutionEntity = executionEntityManager.create();
        counterSignExecutionEntity.setStartTime(new Date());
        counterSignExecutionEntity.setCurrentFlowElement(process.getFlowElement(taskEntity.getTaskDefinitionKey(), true));
        counterSignExecutionEntity.setProcessDefinitionId(secondExecutionEntity.getProcessDefinitionId());
        counterSignExecutionEntity.setProcessInstanceId(secondExecutionEntity.getProcessInstanceId());
        counterSignExecutionEntity.setRootProcessInstanceId(secondExecutionEntity.getRootProcessInstanceId());
        counterSignExecutionEntity.setTenantId(secondExecutionEntity.getTenantId());
        counterSignExecutionEntity.setParentId(secondExecutionEntity.getId());
        counterSignExecutionEntity.setSuspensionState(secondExecutionEntity.getSuspensionState());// 保持原先挂起/激活状态
        counterSignExecutionEntity.setActive(true);
        counterSignExecutionEntity.setScope(false);
//        counterSignExecutionEntity.setConcurrent(true);
        executionEntityManager.insert(counterSignExecutionEntity);

        // 创建加签的任务
        TaskEntity counterSignTaskEntity = taskEntityManager.create();
        counterSignTaskEntity.setName(taskEntity.getName());
        counterSignTaskEntity.setCreateTime(new Date());
        counterSignTaskEntity.setTaskDefinitionKey(taskEntity.getTaskDefinitionKey());
        counterSignTaskEntity.setProcessDefinitionId(taskEntity.getProcessDefinitionId());
        counterSignTaskEntity.setProcessInstanceId(taskEntity.getProcessInstanceId());
        counterSignTaskEntity.setExecutionId(counterSignExecutionEntity.getId());
        counterSignTaskEntity.setExecution(counterSignExecutionEntity);
        counterSignTaskEntity.setAssignee(assignee);
        taskEntityManager.insert(counterSignTaskEntity);

        int nrOfInstances = ActivitiUtil.getLoopVariable(secondExecutionEntity, "nrOfInstances");
        // 设置loopCounter
        List<ExecutionEntity> thirdExecutionEntities = executionEntityManager.findChildExecutionsByParentExecutionId(thirdExecutionEntity.getParentId());
        List<Integer> loopCounters = thirdExecutionEntities.stream().map(e -> ActivitiUtil.getLoopVariable(e, "loopCounter")).collect(Collectors.toList());
        loopCounters.sort(Comparator.reverseOrder());
        ActivitiUtil.setLoopVariable(counterSignExecutionEntity, "loopCounter", loopCounters.get(0) + 1);
        ActivitiUtil.setLoopVariable(secondExecutionEntity, "nrOfInstances", ++nrOfInstances);
        return null;
    }
}
