package com.danlu.dleye.core.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class RedisClient {

    private static Logger logger = LoggerFactory.getLogger(RedisClient.class);
    private JedisPool jedisPool;

    public void init() {
        if (null == this.jedisPool) {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxIdle(200);
            config.setMaxTotal(1024);
            config.setMaxWaitMillis(1000 * 100);
            config.setTestOnBorrow(true);
            this.jedisPool = new JedisPool(config, "localhost", 6379);
        }
    }

    @SuppressWarnings("deprecation")
    public <T> void set(String key, final T value, int time) {
        Jedis jedis = null;
        boolean isOK = true;
        try {
            jedis = jedisPool.getResource();
            String jsonString = JSON.toJSONString(value, SerializerFeature.WriteDateUseDateFormat,
                SerializerFeature.DisableCircularReferenceDetect);
            if (null != jedis) {
                jedis.setex(key, time, jsonString);
            } else {
                init();
            }
        } catch (Exception e) {
            logger.error("set key:" + key + " is exception:" + e.toString());
            isOK = false;
        } finally {
            if (null != jedis) {
                if (isOK) {
                    jedisPool.returnResource(jedis);
                } else {
                    jedisPool.returnBrokenResource(jedis);
                }
            }
        }
    }

    @SuppressWarnings("deprecation")
    public <T> void set(String key, final T value) {
        Jedis jedis = null;
        boolean isOK = true;
        try {
            jedis = jedisPool.getResource();
            String jsonString = JSON.toJSONString(value, SerializerFeature.WriteDateUseDateFormat,
                SerializerFeature.DisableCircularReferenceDetect);
            if (null != jedis) {
                jedis.set(key, jsonString);
            } else {
                init();
            }
        } catch (Exception e) {
            logger.error("set key:" + key + " is exception:" + e.toString());
            isOK = false;
        } finally {
            if (null != jedis) {
                if (isOK) {
                    jedisPool.returnResource(jedis);
                } else {
                    jedisPool.returnBrokenResource(jedis);
                }
            }
        }
    }

    @SuppressWarnings("deprecation")
    public <T> T get(final String key, final TypeReference<T> type) {
        Jedis jedis = null;
        boolean isOK = true;
        try {
            jedis = jedisPool.getResource();
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
            isOK = false;
            return null;
        } finally {
            if (null != jedis) {
                if (isOK) {
                    jedisPool.returnResource(jedis);
                } else {
                    jedisPool.returnBrokenResource(jedis);
                }
            }
        }
    }
}
