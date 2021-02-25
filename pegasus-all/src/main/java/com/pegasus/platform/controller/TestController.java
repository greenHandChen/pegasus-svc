package com.pegasus.platform.controller;

import com.pegasus.common.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import smartbi.sdk.ClientConnector;
import smartbi.sdk.InvokeResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by enHui.Chen on 2020/1/18.
 */
@Slf4j
@RestController
@RequestMapping("/v1/external")
public class TestController {
    /**
     * @Author: enHui.Chen
     * @Description: 测试HZERO, 外部服务调用
     * @Data 2019/9/6
     */
    @PostMapping("/test/external")
    public ResponseEntity<Map<String, Object>> external(@RequestBody List<Map<String,Object>> map) {
//        log.info("{}", JsonUtil.toString(map));
        Map<String, Object> map1 = new HashMap<>();
//        map1.put("test1", "测试字段1");
//        map1.put("test2", "测试字段2");
//        map1.put("test3", map);
        ClientConnector conn = new ClientConnector("");
        boolean open = conn.open("test", ""); // 必须以拥有系统管理员角色的用户登录，其他用户登录调用接口获取的token是null
        if (open) {
            InvokeResult result = conn.remoteInvoke("LoginTokenModule", "generateLoginToken", new Object[]{"test"}); // 用户名
            System.out.println(result.getResult());//result.getResult()就是demo用户的token
            conn.close();
        } else {
            //admin用户登录失败
            System.out.println("登录失败");
        }
        return new ResponseEntity<>(map1, HttpStatus.OK);
    }
}