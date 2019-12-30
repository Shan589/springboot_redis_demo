/*
 * Copyright © 1998 - 2018 Tencent. All Rights Reserved
 * www.tencent.com
 * All rights reserved.
 */
package com.tencent.tusi.springboot_demo_redis.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author shanpeng
 */
@Component
public class RedisUtil {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    public RedisUtil() {
    }

    private static RedisUtil redisUtil;

    @PostConstruct
    public void init() {
        redisUtil = this;
    }

    private RedisTemplate<Object, Object> getRedisTemplate() {
        return redisUtil.redisTemplate;
    }

    private StringRedisTemplate getStringRedisTemplate() {
        return redisUtil.stringRedisTemplate;
    }

    public void setObject(String key, Object value) {
        if (StringUtils.isEmpty(key) || value == null) {
            return;
        }
        getRedisTemplate().opsForValue().set(key, value);
    }

    public Object getObject(String key) {
        return getRedisTemplate().opsForValue().get(key);
    }

    public void setZSet(String key, Map<String, Double> value) {
        if (StringUtils.isEmpty(key) || value == null) {
            return;
        }
        HashMap<String, Double> stringDoubleHashMap = (HashMap) value;
        for (String hashKey :
                stringDoubleHashMap.keySet()) {
            getStringRedisTemplate().opsForZSet().add(key, hashKey, stringDoubleHashMap.get(hashKey));
        }
    }

    public void setMap(String key, Map<String, String> value) {
        if (StringUtils.isEmpty(key) || value == null) {
            return;
        }
        HashMap<String, String> stringStringHashMap = (HashMap) value;
        for (String hashKey :
                stringStringHashMap.keySet()) {
            getStringRedisTemplate().opsForHash().put(key, hashKey, stringStringHashMap.get(hashKey));
        }
    }

    public void setList(String key, List<String> value, Boolean sethead) {
        if (StringUtils.isEmpty(key) || value == null) {
            return;
        }
        List<String> listValue = (List<String>) value;
        if (sethead) {
            for (String string :
                    listValue) {
                getStringRedisTemplate().opsForList().leftPush(key, string);
            }
        } else {
            for (String string :
                    listValue) {
                getStringRedisTemplate().opsForList().rightPush(key, string);
            }
        }
    }

    public void setSet(String key, Set<String> value) {
        if (StringUtils.isEmpty(key) || value == null) {
            return;
        }
        Set<String> strings = value;
        for (String s :
                strings) {
            getStringRedisTemplate().opsForSet().add(key, s);
        }
    }

    public void setString(String key, String value) {
        setString(key, value, null);
    }

    private void setString(String key, String value, Long time) {
        if (StringUtils.isEmpty(key) || value == null) {
            return;
        }
        String stringValue = value;
        if (time == null) {
            getStringRedisTemplate().opsForValue().set(key, value);
        } else {
            getStringRedisTemplate().opsForValue().set(key, value, time, TimeUnit.SECONDS);
        }
    }

    // 根据key获取List集合
    public List<String> getList(String key) {
        return getStringRedisTemplate().opsForList().range(key, 0, getListSize(key));
    }

    private long getListSize(String key) {
        return getStringRedisTemplate().opsForList().size(key);
    }

    // 根据key查找list中的元素，并且删除
    // count位置的值，只有相等才会删除
    public void getListRemover(String key, Long count, String value) {
        getStringRedisTemplate().opsForList().remove(key, count, value);
    }

    // 根据key获取字符串
    public String getValue(String key) {
        return getStringRedisTemplate().opsForValue().get(key);
    }

    // 根据key获取过期时间
    public Long getExpire(String key) {
        return getStringRedisTemplate().getExpire(key);
    }

    // 根据key获取过期时间，并指定返回的时间格式
    public Long getExpireWithSeconds(String key) {
        return getStringRedisTemplate().getExpire(key, TimeUnit.SECONDS);
    }

    // 根据key删除缓存
    public void deleteByKey(String key) {
        getStringRedisTemplate().delete(key);
    }

    // 检查key是否存在
    public boolean isExistKey(String key) {
        return getStringRedisTemplate().hasKey(key);
    }

    // 设置过期时间
    public void setExpire(String key, Long expire) {
        getStringRedisTemplate().expire(key, expire, TimeUnit.SECONDS);
    }

    // 根据key获取set集合
    public Set<String> getSetByKey(String key) {
        return getStringRedisTemplate().opsForSet().members(key);
    }

    // 根据key查看集合中是有指定的数据
    public boolean isExistValueByKey(String key, String value) {
        return getStringRedisTemplate().opsForSet().isMember(key, value);
    }

}
