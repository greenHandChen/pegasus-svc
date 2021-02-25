package com.pegasus.activiti.listener;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: enHui.Chen
 * @Description: 消息通知
 * @Data 2019/11/4
 */
@Slf4j
public class MessageNoticeListener {

    /**
     * @Author: enHui.Chen
     * @Description: 通知指定人
     * @Data 2019/11/4
     */
    public void noticeUser(String employeeCode) {
        log.info("员工编号:{}", employeeCode);
    }

    /**
     * @Author: enHui.Chen
     * @Description: 通知指定人员组
     * @Data 2019/11/4
     */
    public void noticeUserGroup(String userGroupCode) {
        log.info("人员组编号:{}", userGroupCode);
    }

}
