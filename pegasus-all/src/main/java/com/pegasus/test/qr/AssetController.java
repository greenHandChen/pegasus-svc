package com.pegasus.test.qr;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 资产/设备基本信息 管理 API
 *
 * @author zhisheng.zhang@hand-china.com 2019-01-24 16:43:37
 */
@Slf4j
@RestController("assetController.v1")
@RequestMapping("/asset")
public class AssetController {

    @Autowired
    private AssetService assetService;

    @ApiOperation(value = "资产台账标签图片生成")
    @GetMapping("/qrcode-image")
    public void qrcode(@RequestParam(required = false) String codeType, Asset asset, HttpServletRequest request, HttpServletResponse response) {
        String filePath = "/logo/logo.png";
        assetService.qrcodeImage(codeType, asset,request, response, filePath);
    }

    @ApiOperation(value = "资产台账生成二维码PDF-批量")
    @PostMapping("/batch-qr-code-pdf")
    public void batchQrCodePdf(@RequestParam List<Long> assetIds, HttpServletRequest request, HttpServletResponse response) {
        assetService.batchQrCodePdf(assetIds, request, response);
    }

    @ApiOperation(value = "资产台账生成二维码PDF-批量")
    @PostMapping("/amount-of-transaction")
    public void amountOfTransaction() {
        // 回滚
        boolean b = assetService.amountOfTransaction();
        // 根据结果进行是否回滚
        if(b){
            assetService.noRollTransaction();
        }
    }
}
