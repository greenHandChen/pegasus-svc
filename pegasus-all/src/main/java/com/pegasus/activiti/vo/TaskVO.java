package com.pegasus.activiti.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by enHui.Chen on 2019/9/18.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskVO {
    private String id;// 任务id
    private String processInstanceId;// 流程实例id
    private String processName;// 流程名称
    private String name;// 审批环节
    private String startUserName;// 申请人
    private String createdTime;// 创建日期
    private Integer priority;// 优先级
    private String assigneeName;// 任务持有人
    private String assignee;// 任务持有人
}
