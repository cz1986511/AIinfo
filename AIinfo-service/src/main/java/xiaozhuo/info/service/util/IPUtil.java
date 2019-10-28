package xiaozhuo.info.service.util;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;

public class IPUtil {

	private static String URL = "http://ip.taobao.com/service/getIpInfo.php?ip=";

	private static String UNKNOWN = "unknown";

	private static String TXURL = "https://apis.map.qq.com/ws/location/v1/ip?key=VHOBZ-JX7WK-DJ5J2-AC3BO-3L53E-76FKP&ip=";

	public static String getCityCodeByIP(String ip) {
		if (StringUtils.isBlank(ip)) {
			return null;
		}
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			String requestURL = String.format("%s%s", URL, ip);
			HttpGet getMethod = new HttpGet(requestURL);
			getMethod.addHeader("Content-Type", "application/json");
			HttpResponse response = httpClient.execute(getMethod);
			response.addHeader("Content-Type", "text/html;charset=UTF-8");
			if (null != response && response.getStatusLine().getStatusCode() == 200) {
				String responseBody = EntityUtils.toString(response.getEntity(), "UTF-8");
				JSONObject jsonObject = JSONObject.parseObject(responseBody);
				return jsonObject.getJSONObject("data").toJSONString();
			}
		} catch (Exception e) {
			// log.error("getCityCodeByIP ip:[{}] is Exception:{}", ip, e.toString());
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static String getIPFromHttpServlet(HttpServletRequest request) {
		if (null == request) {
			return null;
		}
		String ip = null;
		try {
			String ipAddresses = request.getHeader("X-Forwarded-For");
			if (ipAddresses == null || ipAddresses.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddresses)) {
				// Proxy-Client-IP：apache 服务代理
				ipAddresses = request.getHeader("Proxy-Client-IP");
			}
			if (ipAddresses == null || ipAddresses.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddresses)) {
				// WL-Proxy-Client-IP：weblogic 服务代理
				ipAddresses = request.getHeader("WL-Proxy-Client-IP");
			}
			if (ipAddresses == null || ipAddresses.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddresses)) {
				// HTTP_CLIENT_IP：有些代理服务器
				ipAddresses = request.getHeader("HTTP_CLIENT_IP");
			}
			if (ipAddresses == null || ipAddresses.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddresses)) {
				// X-Real-IP：nginx服务代理
				ipAddresses = request.getHeader("X-Real-IP");
			}
			// 有些网络通过多层代理，那么获取到的ip就会有多个，一般都是通过逗号（,）分割开来，并且第一个ip为客户端的真实IP
			if (ipAddresses != null && ipAddresses.length() != 0) {
				ip = ipAddresses.split(",")[0];
			}
			// 还是不能获取到，最后再通过request.getRemoteAddr();获取
			if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddresses)) {
				ip = request.getRemoteAddr();
			}
		} catch (Exception e) {
			// log.info("getIPFromHttpServlet is Exception:{}", e.toString());
		}
		return ip;
	}

	public static String getTXCityCodeByIp(String ip) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			String requestURL = String.format("%s%s", TXURL, ip);
			HttpGet getMethod = new HttpGet(requestURL);
			HttpResponse response = httpClient.execute(getMethod);
			if (null != response && response.getStatusLine().getStatusCode() == 200) {
				return EntityUtils.toString(response.getEntity(), "UTF-8");
			}
		} catch (Exception e) {
			// log.error("getTXCityCodeByIp ip:[{}] is Exception:{}", ip, e.toString());
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static void main(String[] args) {
		String ip = "140.243.100.209";
		getCityCodeByIP(ip);
		getTXCityCodeByIp(ip);
	}

}
