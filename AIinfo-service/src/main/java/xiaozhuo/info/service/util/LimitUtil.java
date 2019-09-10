package xiaozhuo.info.service.util;

import java.util.concurrent.ConcurrentHashMap;

import com.google.common.util.concurrent.RateLimiter;

public class LimitUtil {

	public static ConcurrentHashMap<String, RateLimiter> limiters = new ConcurrentHashMap<String, RateLimiter>();

	static {
		createResourceLimiter("req", 5000);
	}

	public static void createResourceLimiter(String resource, double qps) {
		if (limiters.contains(resource)) {
			limiters.get(resource).setRate(qps);
		} else {
			RateLimiter rateLimiter = RateLimiter.create(qps);
			limiters.putIfAbsent(resource, rateLimiter);
		}
	}

	/*
	 * 获取令牌
	 */
	public static boolean getRate() {
		return limiters.get("req").tryAcquire();
	}

}
