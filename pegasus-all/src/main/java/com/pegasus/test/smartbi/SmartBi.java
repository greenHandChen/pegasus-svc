package com.pegasus.test.smartbi;

import smartbi.sdk.ClientConnector;
import smartbi.sdk.InvokeResult;

/**
 * Created by enHui.Chen on 2020/4/23.
 */
public class SmartBi {
    public static void main(String[] args) {
        ClientConnector conn = new ClientConnector("test");
        boolean open = conn.open("test", "test"); // 必须以拥有系统管理员角色的用户登录，其他用户登录调用接口获取的token是null
        if (open) {
            InvokeResult result = conn.remoteInvoke("tt", "tt", new Object[]{"test"}); // 用户名
            System.out.println(result.getResult());//result.getResult()就是demo用户的token
            conn.close();
        } else {
            //admin用户登录失败
            System.out.println("登录失败");
        }
//        SpringApplication.run(PegasusApplication.class);
    }
}
