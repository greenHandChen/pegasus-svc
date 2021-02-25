package com.pegasus.test.qr;

/**
 * description
 *
 * @author zhisheng.zhang@hand-china.com
 */
public interface Constants {

    String DEFAULT_PARENT_ID = "0";
    Integer DEFAULT_LEVEL_NUMBER = 0;
    /**
     * 单批最大导入数量
     */
    Integer MAX_IMPORT_SIZE = 500;
    /**
     * 工作流流程定义值集
     */
    String PROCESS_DEFINITION = "HWFP.PROCESS_DEFINITION";

    Long DEFAULT_PAGE = 0L;

    Long DEFAULT_SIZE = 10L;

    interface AssetClassify {
        /**
         * 所有
         */
        String ALL = "all";
        /**
         * 基础信息
         */
        String BASE_MSG = "base_msg";
        /**
         * 管理跟踪
         */
        String MANAGE_TRACK = "manage_track";
        /**
         * 属性说明
         */
        String ATTRIBUTE_DES = "attribute_des";
        /**
         * 源码跟踪
         */
        String SOURCE_TRACK = "source_track";
    }

    /**
     * 资产标签/铭牌规则
     */
    interface VisualLabelRuleCode {
        /**
         * 手动输入指定铭牌号
         */
        String MANUAL_INPUT_NUM = "MANUAL_INPUT_NUM";
        /**
         * =资产编号
         */
        String ASSET_NUM = "ASSET_NUM";
        /**
         * =序列号
         */
        String SERIES_NUM = "SERIES_NUM";
        /**
         * =车架号
         */
        String VEHICLE_NUM = "VEHICLE_NUM";
        /**
         * =发动机号
         */
        String ENGINE_NUM = "ENGINE_NUM";
        /**
         * =其他跟踪编号
         */
        String OTHER_NUM = "OTHER_NUM";
    }

    interface AssetSource {
        /**
         * 购置
         */
        String PURCHASE = "PURCHASE";
        /**
         * 捐赠获得
         */
        String DONATE = "DONATE";
        /**
         * 建造/开发
         */
        String BUILD_AND_DEVELOPMENT = "BUILD_AND_DEVELOPMENT";
        /**
         * 盘盈
         */
        String INVENTORY_PROFIT = "INVENTORY_PROFIT";
        /**
         * 内部调配
         */
        String INTERNAL_DEPLOYMENT = "INTERNAL_DEPLOYMENT";
        /**
         * 经营租赁
         */
        String OPERATING_LEASE = "OPERATING_LEASE";
        /**
         * 临时借入
         */
        String TEMPORARY_BORROW = "TEMPORARY_BORROW";
        /**
         * 厂家投放
         */
        String FACTORY_DELIVERY = "FACTORY_DELIVERY";
        /**
         * 其他
         */
        String OTHERS = "OTHERS";
    }

    interface AssetSourceType {
        /**
         * 盘盈
         */
        String INVENTORY_PROFIT = "80";

        /**
         * 内部购入
         */
        String INTERNAL_PURCHASE = "20";

        /**
         * 在建工程
         */
        String CONSTRUCTION_IN_PROGRESS = "40";
    }

    interface AcceptanceType {
        /**
         * 盘盈验收
         */
        String ACCEPTANCE_INVENTORY_PROFIT = "盘盈验收";

        /**
         * 购置验收
         */
        String ACCEPTANCE_INTERNAL_PURCHASE = "购置验收";

        /**
         * 改造验收
         */
        String ACCEPTANCE_CONSTRUCTION_IN_PROGRESS = "改造验收";
    }

    interface AafmErrorCode {
        /**
         * 同租户内资产编号重复
         */
        String DUPLICATE_ASSET_NUM = "duplicate_asset_num";
        /**
         * 同租户内可视标签/铭牌重复
         */
        String DUPLICATE_VISUAL_LABEL = "duplicate_visual_label";
        /**
         * 可视标签/铭牌不能为空
         */
        String VISUAL_LABEL_IS_MUST = "visual_label_is_must";
        /**
         * 查询资产失败
         */
        String SELECT_ASSET_BY_ASSET_ID_FAILED = "select_asset_by_asset_id_failed";
        /**
         * 查询资产行失败
         */
        String SELECT_LINEAR_BY_ASSET_ID_FAILED = "select_linear_by_asset_id_failed";

        /**
         * 产品类别名重复
         */
        String CATEGORY_NAME_DUPLICATED = "category_name_duplicated";
        /**
         * 产品类别代码重复
         */
        String CATEGORY_CODE_DUPLICATED = "category_code_duplicated";
        /**
         * 父级不可用
         */
        String AAFM_PARENT_CATEGORY_NOT_ENABLE = "aafm_parent_category_not_enable";

        /**
         * 专业分类组织重复分配
         */
        String AAFM_ASSET_SPECIALTY_ORG_DUPLICATED = "asset_specialty_org_duplicated";
        /**
         * 专业分类名称重复
         */
        String AAFM_ASSET_SPECIALTY_NAME_DUPLICATED = "asset_specialty_name_duplicated";
        /**
         * 启用编号后编码规则为空
         */
        String EMPTY_CODE_RULE = "empty_code_rule";
        /**
         * 资产状态名称重复
         */
        String DUPLICATE_ASSET_STATUS_NAME = "duplicate_asset_status_name";
        /**
         * 资产组编号重复
         */
        String ASSET_SET_NUM_DUPLICATED = "asset_set_num_duplicated";
        /**
         * 资产组名字重复
         */
        String ASSET_SET_NAME_DUPLICATED = "asset_set_name_duplicated";

        /**
         * 属性组名重复
         */
        String ATTRIBUTE_SET_NAME_DUPLICATED = "attribute_set_name_duplicated";
        /**
         * 属性组代码重复
         */
        String ATTRIBUTE_SET_CODE_DUPLICATED = "attribute_set_code_duplicated";
        /**
         * 属性名重复
         */
        String ATTRIBUTE_LINE_NAME_DUPLICATED = "attribute_line_name_duplicated";
        /**
         * 属性行号重复
         */
        String ATTRIBUTE_LINE_NUMBER_DUPLICATED = "attribute_line_number_duplicated";
        /**
         * 行代码重复
         */
        String ATTRIBUTE_LINE_CODE_DUPLICATED = "attribute_line_code_duplicated";
        /**
         * 当attribute_type为值集时，值集编码必输
         */
        String ATTRIBUTE_LINE_LOV_VALUE_IS_REQUIRED = "attribute_line_lov_value_is_required";
        /**
         * 事件类型代码重复
         */
        String TRANSACTION_TYPE_CODE_DUPLICATED = "transaction_type_code_duplicated";
        /**
         * 事件类型代码重复
         */
        String TRANSACTION_TYPE_ENABLED = "transaction_type_enabled";
        /**
         * 资产动态字段重复
         */
        String ASSET_DYNAMIC_COLUMN_DUPLICATED = "asset_dynamic_column_duplicated";
        /**
         * 事件类型字段重复
         */
        String TRANSACTION_TYPE_FIELD_COLUMN_DUPLICATED = "transaction_type_field_column_duplicated";
        /**
         * 资产专业-归口管理人员分配(行)重复
         */
        String ASSET_SPECIALTY_MANAGER_DUPLICATED = "asset_specialty_manager_duplicated";

        /**
         * 设备/资产权限 错误
         */
        String ASSET_ACL_NO_OPERATION_RIGHT = "asset_acl_no_operation_right";
    }


    /**
     * 针对资产的权限类型
     */
    public static interface AssetAclType {
        /**
         * 无权限
         */
        String NONE = "NONE";
        /**
         * 查看
         */
        String VIEW = "VIEW";
        /**
         * 查看编辑
         */
        String ALL = "ALL";
    }

    public static interface assetRole {
        /**
         * 所有者
         */
        String ROLE_OWNER = "ROLE_OWNER";
        /**
         * 使用者
         */
        String ROLE_USERS = "ROLE_USERS";
        /**
         * 资产专业归类
         */
        String ROLE_SPE = "ROLE_SPE";
        /**
         * 部门管理者
         */
        String ROLE_DEPT = "ROLE_DEPT";
        /**
         * 全局
         */
        String ROLE_GLOBAL = "ROLE_GLOBAL";
    }

    /**
     * 文件上传时关联的BucketName
     */
    interface BucketName {
        /**
         * 服务申请单
         */
        String amtcServiceApply = "amtc-service-apply";
        /**
         * 工单
         */
        String amtcWorkOrder = "amtc-work-order";
        /**
         * 工序
         */
        String amtcWorkOrderWoop = "amtc-work-order-woop";
        /**
         * 资产移交归还
         */
        String aatnAssetHandoverLine = "aatn-asset-handover-line";
        /**
         * 资产调拨转移
         */
        String aatnTransferOrderLine = "aatn-transfer-order-line";
        /**
         * 报废资产
         */
        String aatnAssetScrapLine = "aatn-asset-scrap-line";
        /**
         * 资产信息变更
         */
        String aatnAssetStatusChangeLine = "aatn-asset-status-change-line";
        /**
         * 资产处置单
         */
        String aatnDisposeOrderLine = "aatn-dispose-order-line";
    }

    /**
     * 审批流的审批结果
     */
    interface WorkflowApproveResult {
        /**
         * 同意
         */
        String APPROVED = "Approved";
        /**
         * 拒绝
         */
        String REJECTED = "Rejected";
    }

    /**
     * 事务处理类型的流程启动选项
     */
    interface WorkflowOption {
        /**
         * 不启用工作流
         */
        String DISABLE = "DISABLE";
        /**
         * 启用平台工作流
         */
        String ENABLE_PLATFORM = "ENABLE_PLATFORM";
        /**
         * 启用外部工作流
         */
        String ENABLE_EXTERNAL = "ENABLE_EXTERNAL";
    }


    /**
     * @Author: enHui.Chen
     * @Description: 标签相关
     * @Data 2020/4/13
     */
    interface AssetLabel {
        // 每页标签个数
        int SIZE_OF_LABLE = 3;
        // 字体路径
        String FONT_PATH = "font/SIMHEI.ttf";
        // 内容编码模板
        String $CONTENT = "${content}";
        // encode资产编码模板
        String $ENCODE_ASSET_NUM = "${encodeAssetNum}";
        // 资产编码模板
        String $ASSET_NUM = "${assetNum}";
        // 资产名称模板
        String $ASSET_NAME = "${assetName}";
        // 资产品牌模板
        String $BRAND = "${brand}";
        // 资产规格模板
        String $MODEL = "${model}";
        // 二维码生成路径
        String qrCodePath = "http://test-eam1.yimidida.com/aatn/v1/0/asset-info/qrcode-image?codeType=QR&amp;assetNum=" + $ENCODE_ASSET_NUM;

        // 内容模板
        String YMDD_CONTENT_TEMPLATE = "<div style=\"margin-left: 30.0pt;margin-bottom: 11.0pt;\">\n" +
                "<div style=\"float:left\">\n" +
                "<p class=\"c0\"><img src=\"" + qrCodePath + "\" style=\"width:60pt;height:60pt;\" alt=\"An Image\" ></img><br/></p>\n" +
                "</div>\n" +
                "<div style=\"float:left\">\n" +
                "<p class=\"t1\"><span class=\"c2\">壹米滴答</span></p>\n" +
                "<p class=\"c1\"><span class=\"c2\">资产条码:" + $ASSET_NUM + "</span></p>\n" +
                "<p class=\"c1\"><span class=\"c2\">资产名称:" + $ASSET_NAME + "</span></p>\n" +
                "<p class=\"c1\"><span class=\"c2\">品牌:" + $BRAND + "</span></p>\n" +
                "<p class=\"c1\"><span class=\"c2\">规格:" + $MODEL + "</span></p>\n" +
                "</div>\n" +
                "</div>\n" +
                "<div style=\"clear:both\"/>\n";

        // html模板
        String YMDD_HTML_TEMPLATE = "<!DOCTYPE html >\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                "<head>\n" +
                "<style type=\"text/css\">\n" +
                ".c0 {margin-top: 0.0pt;margin-bottom: 0.0pt;margin-right: 5.899pt;}\n" +
                ".c1 {line-height: 7.514pt;margin-top: 0.0pt;margin-bottom: 4.0pt;font-size: 6pt;}\n" +
                ".t1 {line-height: 7.514pt;margin-top: 0.0pt;margin-left: 19.0pt;margin-bottom: 10.0pt;font-size: 10pt;}\n" +
                "  body { padding:0; margin:0; color: #000000;font-family:Microsoft YaHei; @page {size:20mm, 35mm;}} \n" +
                "</style>\n" +
                "</head>\n" +
                "<body>\n" +
                "<div style=\"margin-top: 47.0pt;\">\n" +
                "<div style=\"margin-left: 30.0pt;margin-bottom: 11.0pt;\">\n" +
                "<div style=\"float:left\">\n" +
                "<p class=\"c0\"><img src=\"http://test-eam1.yimidida.com/aatn/v1/0/asset-info/qrcode-image?codeType=QR&amp;assetNum=asset.getAssetNum%28%29\" style=\"width:60pt;height:60pt;\" alt=\"An Image\" /><br/></p>\n" +
                "</div>\n" +
                "<div style=\"float:left\">\n" +
                "<p class=\"t1\"><span class=\"c2\">壹米滴答</span></p>\n" +
                "<p class=\"c1\"><span class=\"c2\">资产条码:asset.getAssetNum()</span></p>\n" +
                "<p class=\"c1\"><span class=\"c2\">资产名称:asset.getAssetName()</span></p>\n" +
                "<p class=\"c1\"><span class=\"c2\">品牌:asset.getBrand()</span></p>\n" +
                "<p class=\"c1\"><span class=\"c2\">规格:asset.getModel()</span></p>\n" +
                "</div>\n" +
                "</div>\n" +
                "<div style=\"clear:both\"/>\n" +
                "\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>\n";
    }
}