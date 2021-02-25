package com.pegasus.test.qr;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 资产/设备基本信息
 *
 * @author zhisheng.zhang@hand-china.com 2019-01-24 16:43:37
 */
@ApiModel("资产/设备基本信息")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Asset {
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------
    private String assetNum;
    private String assetName;
}