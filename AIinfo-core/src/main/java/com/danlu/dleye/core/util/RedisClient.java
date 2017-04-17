package com.danlu.dleye.core.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class RedisClient {

    private static Logger logger = LoggerFactory.getLogger(RedisClient.class);
    private Jedis jedis = null;

    public void init() {
        if (null == this.jedis) {
            this.jedis = new Jedis("localhost");
        }
    }

    public <T> void set(String key, final T value, int time) {
        String jsonString = JSON.toJSONString(value, SerializerFeature.WriteDateUseDateFormat,
            SerializerFeature.DisableCircularReferenceDetect);
        if (null != jedis) {
            jedis.setex(key, time, jsonString);
        } else {
            init();
        }
    }

    public <T> T get(final String key, final TypeReference<T> type) {
        try {
            if (null != jedis) {
                String value = jedis.get(key);
                if (StringUtils.isEmpty(value)) {
                    return null;
                } else {
                    return JSON.parseObject(value, type);
                }
            } else {
                init();
                return null;
            }
        } catch (Exception e) {
            logger.error("get key:" + key + " is exception:" + e.toString());
            return null;
        }
    }
}
