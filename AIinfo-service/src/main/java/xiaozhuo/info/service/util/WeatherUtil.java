package xiaozhuo.info.service.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import xiaozhuo.info.persist.base.WeatherInfo;
import xiaozhuo.info.service.WeatherInfoService;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@SuppressWarnings("deprecation")
@Component
@Configurable
@EnableScheduling
@Service
public class WeatherUtil {

	private static Logger log = LoggerFactory.getLogger(WeatherUtil.class);

	@Autowired
	private WeatherInfoService weatherInfoService;

	private static String DEFAULTCITY = "chengdu";

	@Scheduled(cron = "0 5 0 * * ?")
	public List<JSONObject> getTodayWeatherInfo() {
		List<JSONObject> list = new ArrayList<JSONObject>();
		try {
			JSONObject jsonObject = getWeatherNext(DEFAULTCITY);
			jsonObject.put("now", getWeatherNow(DEFAULTCITY));
			jsonObject.put("suggestion", getSuggestion(DEFAULTCITY));
			list.add(jsonObject);
			RedisClient.set("weather", list, 4200);
			saveWeatherInfo(jsonObject);
		} catch (Exception e) {
			log.error("getTodayWeatherInfo is exception:" + e.toString());
		}
		return list;
	}

	private void saveWeatherInfo(JSONObject weatherInfoJsonObject) {
		try {
			SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
			String dateTime = ft.format(new Date());
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("cityName", "chengdu");
			map.put("dateTime", dateTime);
			List<WeatherInfo> list = weatherInfoService.getWeatherInfos(map);
			if (CollectionUtils.isEmpty(list)) {
				JSONArray daily = weatherInfoJsonObject.getJSONArray("daily");
				JSONObject suggestion = weatherInfoJsonObject
						.getJSONObject("suggestion");
				if (null != daily) {
					JSONObject todayJsonObject = daily.getJSONObject(0);
					WeatherInfo weatherInfo = new WeatherInfo();
					weatherInfo.setCityName("chengdu");
					weatherInfo.setDateTime(dateTime);
					weatherInfo.setCodeDay(todayJsonObject
							.getString("code_day"));
					weatherInfo.setCodeNight(todayJsonObject
							.getString("code_night"));
					weatherInfo.setHighTemperature(todayJsonObject
							.getInteger("high"));
					weatherInfo.setLowTemperature(todayJsonObject
							.getInteger("low"));
					weatherInfo.setTextDay(todayJsonObject
							.getString("text_day"));
					weatherInfo.setTextNight(todayJsonObject
							.getString("text_night"));
					weatherInfo.setWindDirection(todayJsonObject
							.getString("wind_direction"));
					weatherInfo.setStatus("01");
					weatherInfo.setSuggestion(suggestion.toJSONString());
					weatherInfoService.addWeatherInfo(weatherInfo);
				}
			}
		} catch (Exception e) {
			log.error("saveWeatherInfo is exception:" + e.toString());
		}
	}

	@SuppressWarnings({ "resource" })
	private JSONObject getWeatherNow(String city) {
		HttpClient httpClient = new DefaultHttpClient();
		try {
			String restUrl = "https://api.seniverse.com/v3/weather/now.json?key=tl9ml0o784jsrc4h&language=zh-Hans&unit=c&location=";
			HttpGet getMethod = new HttpGet(restUrl + city);
			HttpResponse response = httpClient.execute(getMethod);
			if (null != response) {
				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode < 300) {
					String responseBody = EntityUtils.toString(
							response.getEntity(), "UTF-8");
					JSONArray array = (JSONArray) JSON
							.parseObject(responseBody).get("results");
					return array.getJSONObject(0).getJSONObject("now");
				}
			}
		} catch (Exception e) {
			log.error("getSuggestion is exception:" + e.toString());
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
		log.error("getSuggestion is null");
		return null;
	}

	@SuppressWarnings({ "resource" })
	private JSONObject getWeatherNext(String city) {
		HttpClient httpClient = new DefaultHttpClient();
		try {
			String restUrl = "https://api.seniverse.com/v3/weather/daily.json?key=tl9ml0o784jsrc4h&language=zh-Hans&unit=c&start=0&days=5&location=";
			HttpGet getMethod = new HttpGet(restUrl + city);
			HttpResponse response = httpClient.execute(getMethod);
			if (null != response) {
				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode < 300) {
					String responseBody = EntityUtils.toString(
							response.getEntity(), "UTF-8");
					JSONArray array = (JSONArray) JSON
							.parseObject(responseBody).get("results");
					return array.getJSONObject(0);
				}
			}
		} catch (Exception e) {
			log.error("getSuggestion is exception:" + e.toString());
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
		log.error("getSuggestion is null");
		return null;
	}

	@SuppressWarnings({ "resource" })
	private JSONObject getSuggestion(String city) {
		HttpClient httpClient = new DefaultHttpClient();
		try {
			String restUrl = "https://api.seniverse.com/v3/life/suggestion.json?key=tl9ml0o784jsrc4h&language=zh-Hans&location=";
			HttpGet getMethod = new HttpGet(restUrl + city);
			HttpResponse response = httpClient.execute(getMethod);
			if (null != response) {
				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode < 300) {
					String responseBody = EntityUtils.toString(
							response.getEntity(), "UTF-8");
					JSONArray array = (JSONArray) JSON
							.parseObject(responseBody).get("results");
					return array.getJSONObject(0).getJSONObject("suggestion");
				}
			}
		} catch (Exception e) {
			log.error("getSuggestion is exception:" + e.toString());
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
		log.error("getSuggestion is null");
		return null;
	}

}
