package xiaozhuo.info.service.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;

@Component
@Order(value = 1)
@ConfigurationProperties(prefix = "redis")
@Slf4j
public class RedisClient implements CommandLineRunner {

	private static JedisPool jedisPool;
	private static String ip = "localhost";
	private static int port = 6379;

	public void setIp(String ip) {
		RedisClient.ip = ip;
	}

	public void setPort(int port) {
		RedisClient.port = port;
	}

	public static void init() {
		if (null == jedisPool) {
			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxIdle(200);
			config.setMaxTotal(1024);
			config.setMaxWaitMillis(1000 * 100);
			config.setTestOnBorrow(true);
			jedisPool = new JedisPool(config, ip, port);
		}
	}

	@SuppressWarnings("deprecation")
	public static <T> void set(String key, final T value, int time) {
		Jedis jedis = null;
		boolean isOK = true;
		try {
			jedis = jedisPool.getResource();
			String jsonString = JSON.toJSONString(value,
					SerializerFeature.WriteDateUseDateFormat,
					SerializerFeature.DisableCircularReferenceDetect);
			if (null != jedis) {
				jedis.setex(key, time, jsonString);
			} else {
				init();
			}
		} catch (Exception e) {
			log.error("set key:{} is exception:{}", key, e.toString());
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
	public static <T> void set(String key, final T value) {
		Jedis jedis = null;
		boolean isOK = true;
		try {
			jedis = jedisPool.getResource();
			String jsonString = JSON.toJSONString(value,
					SerializerFeature.WriteDateUseDateFormat,
					SerializerFeature.DisableCircularReferenceDetect);
			if (null != jedis) {
				jedis.set(key, jsonString);
			} else {
				init();
			}
		} catch (Exception e) {
			log.error("set key:{} is exception:{}", key, e.toString());
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
	public static <T> T get(final String key, final TypeReference<T> type) {
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
			log.error("get key:{} is exception:{}", key, e.toString());
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

	@Override
	public void run(String... args) throws Exception {
		init();
	}
}
