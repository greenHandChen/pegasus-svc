package com.pegasus.activiti.constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by enHui.Chen on 2019/8/29.
 */
public class ActivitiConstants {
    public static final String APPROVED = "APPROVED";

    public static final String REJECTED = "REJECTED";

    public static final int REVISION = 1;

    public static final String SPLIT_CHAR = ":";

    public static final String NUMBER_OF_REJECTED = "nrOfRejected";

    public static final String NUMBER_OF_APPROVED = "nrOfApproved";

    public static final String ASSIGNEE_LIST = "assigneeList";// 多实例审批链

    public static final String APPROVE_CANDIDATE_APPROVER = "APPROVE_CANDIDATE_APPROVER";

    public static final List<String> OUTCOME_LIST = new ArrayList<>();

    public static final String APPROVE_MARK = "outcome";// 审批标记

    public static final String NODE_JUMP = "节点任务跳转";

    public static final String NODE_FINISH = "节点任务终止";

    public static final String NODE_DELIVERY = "节点任务转交";

    public static final Integer SUSPEND = 2;// 挂起

    public static final Integer ACTIVE = 1;// 激活

    static {
        OUTCOME_LIST.add("APPROVED");
        OUTCOME_LIST.add("REJECTED");
    }
}
