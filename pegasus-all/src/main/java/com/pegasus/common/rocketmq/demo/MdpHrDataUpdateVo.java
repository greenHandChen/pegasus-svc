package com.pegasus.common.rocketmq.demo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: NanJW
 * @Date: 2020/4/20 17:45
 * @Version 1.0
 */
@Data
public class MdpHrDataUpdateVo implements Serializable {
    //主部门编码
    private String orgCode;
    //主部门名称
    private String orgName;
    //HR编码
    private String hrOrgCode;
    //快递编码
    private String omgOrgCode;
    //快运编码
    private String yhOrgCode;
    //上级组织编码
    private String parentCode;
    //上级组织名称
    private String parentName;
    //创建时间
    private Date createrTime;
    //最后修改时间
    private Date latestTime;
    //全路径名称
    private String orgFullName;
    //业务线
    private String businessLine;
    //财务科目编码
    private String financialAccountCode;
    //部门hr状态 1 新增 2 修改 3 作废
    private Integer status;
    //来源 1 HR 2 快递 3 快运
    private Integer sourceType;
}
