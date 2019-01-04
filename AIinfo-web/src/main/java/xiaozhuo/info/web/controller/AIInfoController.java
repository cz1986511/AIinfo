package xiaozhuo.info.web.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import xiaozhuo.info.persist.base.ArticleInfo;
import xiaozhuo.info.service.ArticleInfoService;
import xiaozhuo.info.service.util.Crawler;
import xiaozhuo.info.service.util.OilInfoUtil;
import xiaozhuo.info.service.util.RedisClient;
import xiaozhuo.info.service.util.WeatherUtil;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

@Controller
@RequestMapping("/api")
public class AIInfoController {

	private static Logger logger = LoggerFactory
			.getLogger(AIInfoController.class);
	@Autowired
	private ArticleInfoService articleInfoService;
	@Autowired
	private Crawler crawler;
	@Autowired
	private OilInfoUtil oilInfoUtil;
	@Autowired
	private WeatherUtil weatherUtil;
	private static String DEFAULTKEY = "tl9ml0o784jsrc4h";

	@RequestMapping(value = "/art/list", method = RequestMethod.POST)
	@ResponseBody
	public String getArticleList(HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		JSONObject json = new JSONObject(result);
		String defaultKey = "dKey";
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("offset", 0);
			map.put("limit", 200);
			List<ArticleInfo> resultList = (List<ArticleInfo>) RedisClient.get(
					defaultKey, new TypeReference<List<ArticleInfo>>() {
					});
			if (!CollectionUtils.isEmpty(resultList)) {
				result.put("data", resultList);
			} else {
				List<ArticleInfo> articleInfoList = articleInfoService
						.getArticleInfosByParams(map);
				if (!CollectionUtils.isEmpty(articleInfoList)) {
					result.put("data", articleInfoList);
					RedisClient.set(defaultKey, articleInfoList, 3600);
				}
			}
			result.put("status", 0);
		} catch (Exception e) {
			logger.error("getArticleList is exception:" + e.toString());
			result.put("status", 1);
			result.put("msg", "程序小哥跟老板娘跑了");
		}
		return json.toJSONString();
	}

	@RequestMapping(value = "/weather", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getWeather(HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		JSONObject json = new JSONObject(result);
		String weather = "weather";
		List<JSONObject> list = null;
		try {
			list = (List<JSONObject>) RedisClient.get(weather,
					new TypeReference<List<JSONObject>>() {
					});
			if (!CollectionUtils.isEmpty(list)) {
				result.put("status", 0);
				result.put("data", list);
			} else {
				weatherUtil.getTodayWeatherInfo();
				result.put("status", 1);
				result.put("msg", "程序小哥跟老板娘跑了");
			}
		} catch (Exception e) {
			result.put("status", 1);
			result.put("msg", "程序小哥跟老板娘跑了");
			logger.error("getWeather is exception:" + e.toString());
		}
		return json.toJSONString();
	}

	@RequestMapping(value = "/oilinfo", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getOilInfo(HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		JSONObject json = new JSONObject(result);
		String oilKey = "oilKey";
		JSONObject oilJson = null;
		try {
			oilJson = (JSONObject) RedisClient.get(oilKey,
					new TypeReference<JSONObject>() {
					});
			if (!CollectionUtils.isEmpty(oilJson)) {
				result.put("status", 0);
				result.put("data", oilJson);
			} else {
				result.put("status", 1);
				result.put("msg", "程序小哥跟老板娘跑了");
			}
		} catch (Exception e) {
			result.put("status", 1);
			result.put("msg", "程序小哥跟老板娘跑了");
			logger.error("getOilInfo is exception:" + e.toString());
		}
		return json.toJSONString();
	}

	@RequestMapping(value = "/data", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String makeData(HttpServletRequest request) {
		String token1 = (String) request.getAttribute("token");
		String token2 = request.getParameter("token");
		logger.info("token1:" + token1);
		logger.info("token2:" + token2);
		Map<String, Object> result = new HashMap<String, Object>();
		JSONObject json = new JSONObject(result);
		if (DEFAULTKEY.equals(token2)) {
			try {
				crawler.crawlerInfo();
				oilInfoUtil.getTodayOilInfo();
				weatherUtil.getTodayWeatherInfo();
				result.put("status", 0);
				result.put("msg", "数据更新成功");
			} catch (Exception e) {
				result.put("status", 1);
				result.put("msg", "程序小哥跟老板娘跑了");
				logger.error("makeData is exception:" + e.toString());
			}
		} else {
			result.put("status", 1);
			result.put("msg", "程序小哥跟老板娘跑了!");
		}
		return json.toJSONString();
	}

	public static void main(String[] args) {
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String test = ft.format(new Date());
		System.out.println(test);
	}

}
