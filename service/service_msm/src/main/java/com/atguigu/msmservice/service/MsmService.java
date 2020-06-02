package com.atguigu.msmservice.service;

import java.util.Map;

public interface MsmService {
    //短信发送的方法
    boolean send(Map<String, Object> param, String phone);

}
