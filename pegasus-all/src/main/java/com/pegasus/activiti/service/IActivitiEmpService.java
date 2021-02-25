package com.pegasus.activiti.service;

/**
 * Created by enHui.Chen on 2019/9/20.
 */
public interface IActivitiEmpService {

    /**
     * @Author: enHui.Chen
     * @Description: 获取上级领导
     * @Data 2019/9/20
     */
    Long findManager(String userId);

    /**
     * @Author: enHui.Chen
     * @Description: 获取某一层级领导
     * @Data 2019/9/20
     */
    Long findManagerByLevel(String userId, int level);

}
