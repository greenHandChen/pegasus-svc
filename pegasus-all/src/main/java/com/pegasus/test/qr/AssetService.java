package com.pegasus.test.qr;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 资产/设备基本信息应用服务
 *
 * @author zhisheng.zhang@hand-china.com 2019-01-24 16:43:37
 */
public interface AssetService {

    /**
     * 资产台账标签图片生成
     *
     * @param codeType 生成类型
     * @param asset    资产信息
     * @param request  请求
     * @param response 响应
     * @param filePath 文件位置
     */
    void qrcodeImage(String codeType,Asset asset, HttpServletRequest request, HttpServletResponse response, String filePath);

    /**
     * 资产/设备-维护、查询导出
     *
     * @param tenantId
     * @param assetIds
     * @return
     */
    void batchQrCodePdf(List<Long> assetIds, HttpServletRequest request, HttpServletResponse response);

    /**
     * @Author: enHui.Chen
     * @Description: 部分事务回滚
     * @Data 2020/4/16
     */
    boolean amountOfTransaction();

    /**
     * @Author: enHui.Chen
     * @Description: 不回滚
     * @Data 2020/4/16
     */
    void noRollTransaction();
}
