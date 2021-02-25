package com.pegasus.activiti.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by enHui.Chen on 2019/10/29.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProcessInstanceVO {
    private String id;// 流程实体id
    private String procDefId;// 流程定义Id
    private String taskId;// 任务id
    private String processName;// 流程名称
    private String startUserName;// 任务申请人
    private String startTime;// 开始日期
    private String endTime;// 结束日期
    private Integer suspensionState;// 流程状态
    private String assignees;// 多个任务候选人
    private String name;// 审批环节
    private String taskDefKey;// 审批节点
}
