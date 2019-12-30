/*
 * Copyright © 1998 - 2018 Tencent. All Rights Reserved
 * www.tencent.com
 * All rights reserved.
 */
package com.tencent.tusi.springboot_demo_redis.controller;

import com.tencent.tusi.demo_redis.entity.UserInfo;
import com.tencent.tusi.demo_redis.util.RedisUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author shanpeng
 */
@Controller
@RequestMapping(value = "sys")
public class TestController {

    private RedisUtil redisUtil = new RedisUtil();

    @RequestMapping(value = "/redis", method = RequestMethod.GET)
    @ResponseBody
    public UserInfo testRedis() {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserName("李四");
        userInfo.setPassword("pasdwd");
        userInfo.setRemark("测试用户");
        redisUtil.setObject("user", userInfo);
        return (UserInfo) redisUtil.getObject("user");
    }
}
