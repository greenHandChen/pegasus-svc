package com.pegasus.activiti.custom.cmd;

import com.pegasus.activiti.utils.ActivitiUtil;
import com.pegasus.common.exception.CommonException;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ExecutionEntityManager;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntityManager;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Author: enHui.Chen
 * @Description: 节点减签(多实例) 删除对应的执行流以及任务,多实例数量减1(execution以及task)
 * @Data 2019/11/6
 */
public class MultiNodeCounterSignReduceCommand implements Command<Object> {
    private String taskId;// 任务Id

    public MultiNodeCounterSignReduceCommand(String taskId) {
        this.taskId = taskId;
    }

    @Override
    public Object execute(CommandContext commandContext) {
        TaskEntityManager taskEntityManager = commandContext.getTaskEntityManager();
        ExecutionEntityManager executionEntityManager = commandContext.getExecutionEntityManager();
        // 获取当前task的父执行流(多实例一共有三级执行流,此处获取的是第二级)
        TaskEntity taskEntity = taskEntityManager.findById(taskId);
        ExecutionEntity thirdExecutionEntity = executionEntityManager.findById(taskEntity.getExecutionId());
        ExecutionEntity secondExecutionEntity = thirdExecutionEntity.getParent();
        // 如果只有一个任务不允许删除
        List<ExecutionEntity> thirdExecutionEntities = executionEntityManager.findChildExecutionsByParentExecutionId(secondExecutionEntity.getId());
        if (CollectionUtils.isEmpty(thirdExecutionEntities) || thirdExecutionEntities.size() < 2) {
            throw new CommonException("该节点只存在一个任务不允许进行减签!");
        }

        // 获取当前实例数以及loopCounter
        int nrOfInstances = ActivitiUtil.getLoopVariable(secondExecutionEntity, "nrOfInstances");
        int reduceLoopCounter = ActivitiUtil.getLoopVariable(thirdExecutionEntity, "loopCounter");
        // 剩余实例数-1
        ActivitiUtil.setLoopVariable(secondExecutionEntity, "nrOfInstances", --nrOfInstances);
        // 获取当前loopCounter,大于此执行流loopCounter的执行流各-1
        thirdExecutionEntities.forEach(thirdE -> {
            Integer loopCounter = ActivitiUtil.getLoopVariable(thirdE, "loopCounter");
            // 排除当前执行流
            if (!thirdE.getId().equals(thirdExecutionEntity.getId()) && loopCounter.compareTo(reduceLoopCounter) > 0) {
                ActivitiUtil.setLoopVariable(thirdE, "loopCounter", --loopCounter);
            }
        });

        // 删除执行流
        executionEntityManager.deleteExecutionAndRelatedData(thirdExecutionEntity, "节点减签:" + taskId, true);
        // 删除任务
        taskEntityManager.deleteTask(taskEntity, "节点减签:" + taskId, false, true);
        return null;
    }
}
