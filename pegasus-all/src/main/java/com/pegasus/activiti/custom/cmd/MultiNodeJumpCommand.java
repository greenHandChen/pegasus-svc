package com.pegasus.activiti.custom.cmd;

import com.pegasus.activiti.constant.ActivitiConstants;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ExecutionEntityManager;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntityManager;
import org.activiti.engine.impl.util.ProcessDefinitionUtil;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Author: enHui.Chen
 * @Description: 节点跳转(多实例, 删除当前task, execution) 1.获取实体 2.删除执行流以及相关任务 3.创建新的执行流设置跳转节点
 * @Data 2019/10/25
 */
public class MultiNodeJumpCommand implements Command<Object> {
    private String currentTaskId;
    private String destinationActivitiId;

    public MultiNodeJumpCommand(String currentTaskId, String destinationActivitiId) {
        this.currentTaskId = currentTaskId;
        this.destinationActivitiId = destinationActivitiId;
    }

    @Override
    public Object execute(CommandContext commandContext) {
        // 获取task, execution, 的manager实体
        TaskEntityManager taskEntityManager = commandContext.getTaskEntityManager();
        ExecutionEntityManager executionEntityManager = commandContext.getExecutionEntityManager();
        // 当前task
        TaskEntity taskEntity = taskEntityManager.findById(currentTaskId);

        // 获取execution/删除execution(多实例情况下执行流一共分为三级)
        ExecutionEntity thirdExecutionEntity = executionEntityManager.findById(taskEntity.getExecutionId());// 第三级
        ExecutionEntity secondExecutionEntity = thirdExecutionEntity.getParent();// 第二级
        ExecutionEntity firstExecutionEntity = secondExecutionEntity.getParent();// 第一级

        // 获取task/删除task
        List<ExecutionEntity> thirdExecutionEntities = executionEntityManager.findChildExecutionsByParentExecutionId(thirdExecutionEntity.getParentId());
        if (!CollectionUtils.isEmpty(thirdExecutionEntities)) {
            thirdExecutionEntities.forEach(thirdE -> {
                List<TaskEntity> taskEntityList = taskEntityManager.findTasksByExecutionId(thirdE.getId());
                if (!CollectionUtils.isEmpty(taskEntityList)) {
                    taskEntityList.forEach(te -> taskEntityManager.deleteTask(te, ActivitiConstants.NODE_JUMP, false, true));
                }
            });
        }
        // 删除execution
        executionEntityManager.deleteChildExecutions(secondExecutionEntity, ActivitiConstants.NODE_JUMP, true);
        executionEntityManager.deleteExecutionAndRelatedData(secondExecutionEntity, ActivitiConstants.NODE_JUMP, true);


        // 生成新的执行流
        ExecutionEntity newExecutionEntity = executionEntityManager.create();
        newExecutionEntity.setProcessDefinitionId(firstExecutionEntity.getProcessDefinitionId());
        newExecutionEntity.setProcessInstanceId(firstExecutionEntity.getProcessInstanceId());
        newExecutionEntity.setRootProcessInstanceId(firstExecutionEntity.getRootProcessInstanceId());
        newExecutionEntity.setTenantId(firstExecutionEntity.getTenantId());
        newExecutionEntity.setParentId(firstExecutionEntity.getId());
        newExecutionEntity.setSuspensionState(firstExecutionEntity.getSuspensionState());// 保持原先挂起/激活状态
        newExecutionEntity.setActive(true);
        newExecutionEntity.setScope(false);

        // 设置执行流的当前节点
        Process process = ProcessDefinitionUtil.getProcess(taskEntity.getProcessDefinitionId());
        newExecutionEntity.setCurrentFlowElement(process.getFlowElement(destinationActivitiId, true));
        executionEntityManager.insert(newExecutionEntity);
        commandContext.getAgenda().planContinueProcessInCompensation(newExecutionEntity);
        return newExecutionEntity;
    }
}
