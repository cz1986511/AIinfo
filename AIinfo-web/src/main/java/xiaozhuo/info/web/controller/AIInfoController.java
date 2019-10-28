package xiaozhuo.info.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import xiaozhuo.info.persist.base.ArticleInfo;
import xiaozhuo.info.service.ArticleInfoService;
import xiaozhuo.info.service.util.Constant;
import xiaozhuo.info.service.util.Crawler;
import xiaozhuo.info.service.util.LimitUtil;
import xiaozhuo.info.service.util.OilInfoUtil;
import xiaozhuo.info.service.util.RedisClient;
import xiaozhuo.info.service.util.SsqInfoUtil;
import xiaozhuo.info.service.util.WeatherUtil;

@Controller
@RequestMapping("/api")
public class AIInfoController {

	private static Logger logger = LoggerFactory.getLogger(AIInfoController.class);
	@Autowired
	private SsqInfoUtil ssqInfoUtil;
	@Autowired
	private ArticleInfoService articleInfoService;
	@Autowired
	private Crawler crawler;
	@Autowired
	private OilInfoUtil oilInfoUtil;
	@Autowired
	private WeatherUtil weatherUtil;
	private static String ART_KEY = "dKey";
	private static String WEATHER_KEY = "weather";
	private static String OIL_KEY = "oilKey";
	@Value("${data.token}")
	private String dataToken;

	@RequestMapping(value = "/art/list", method = RequestMethod.POST)
	@ResponseBody
	public String getArticleList(HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		JSONObject json = new JSONObject(result);
		if (!LimitUtil.getRate()) {
			result.put("status", Constant.ERRORCODE2);
			result.put("msg", Constant.ERRORMSG2);
			return json.toJSONString();
		}
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("offset", 0);
			map.put("limit", 200);
			List<ArticleInfo> resultList = (List<ArticleInfo>) RedisClient.get(ART_KEY,
					new TypeReference<List<ArticleInfo>>() {
					});
			if (!CollectionUtils.isEmpty(resultList)) {
				result.put("data", resultList);
			} else {
				List<ArticleInfo> articleInfoList = articleInfoService.getArticleInfosByParams(map);
				if (!CollectionUtils.isEmpty(articleInfoList)) {
					result.put("data", articleInfoList);
					RedisClient.set(ART_KEY, articleInfoList, 3600);
				}
			}
			result.put("status", Constant.SUCESSCODE);
		} catch (Exception e) {
			logger.error("getArticleList is exception:" + e.toString());
			result.put("status", Constant.ERRORCODE1);
			result.put("msg", Constant.ERRORMSG1);
		}
		return json.toJSONString();
	}

	@RequestMapping(value = "/weather", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getWeather(HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		JSONObject json = new JSONObject(result);
		if (!LimitUtil.getRate()) {
			result.put("status", Constant.ERRORCODE2);
			result.put("msg", Constant.ERRORMSG2);
			return json.toJSONString();
		}
		List<JSONObject> list = null;
		try {
			list = (List<JSONObject>) RedisClient.get(WEATHER_KEY, new TypeReference<List<JSONObject>>() {
			});
			if (!CollectionUtils.isEmpty(list)) {
				result.put("data", list);
			} else {
				result.put("data", weatherUtil.getTodayWeatherInfo());
			}
			result.put("status", Constant.SUCESSCODE);
		} catch (Exception e) {
			result.put("status", Constant.ERRORCODE1);
			result.put("msg", Constant.ERRORMSG1);
			logger.error("getWeather is exception:" + e.toString());
		}
		return json.toJSONString();
	}

	@RequestMapping(value = "/oilinfo", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getOilInfo(HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		JSONObject json = new JSONObject(result);
		if (!LimitUtil.getRate()) {
			result.put("status", Constant.ERRORCODE2);
			result.put("msg", Constant.ERRORMSG2);
			return json.toJSONString();
		}
		try {
			JSONObject oilJson = (JSONObject) RedisClient.get(OIL_KEY, new TypeReference<JSONObject>() {
			});
			if (null != oilJson) {
				result.put("status", Constant.SUCESSCODE);
				result.put("data", oilJson);
			} else {
				result.put("status", Constant.ERRORCODE1);
				result.put("msg", Constant.ERRORMSG1);
			}
		} catch (Exception e) {
			result.put("status", Constant.ERRORCODE1);
			result.put("msg", Constant.ERRORMSG1);
			logger.error("getOilInfo is exception:" + e.toString());
		}
		return json.toJSONString();
	}

	@RequestMapping(value = "/data", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String makeData(HttpServletRequest request) {
		String token = request.getParameter("token");
		Map<String, Object> result = new HashMap<String, Object>();
		JSONObject json = new JSONObject(result);
		if (!LimitUtil.getRate()) {
			result.put("status", Constant.ERRORCODE2);
			result.put("msg", Constant.ERRORMSG2);
			return json.toJSONString();
		}
		if (dataToken.equals(token)) {
			try {
				crawler.crawlerInfo();
				oilInfoUtil.getTodayOilInfo();
				weatherUtil.getTodayWeatherInfo();
				result.put("status", Constant.SUCESSCODE);
				result.put("msg", Constant.SUCESSMSG);
			} catch (Exception e) {
				result.put("status", Constant.ERRORCODE1);
				result.put("msg", Constant.ERRORMSG1);
				logger.error("makeData is exception:" + e.toString());
			}
		} else {
			result.put("status", Constant.ERRORCODE1);
			result.put("msg", Constant.ERRORMSG1);
		}
		return json.toJSONString();
	}
	
	@RequestMapping(value = "/data/cp", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String makeCPData(HttpServletRequest request) {
		String token = request.getParameter("token");
		Map<String, Object> result = new HashMap<String, Object>();
		JSONObject json = new JSONObject(result);
		if (!LimitUtil.getRate()) {
			result.put("status", Constant.ERRORCODE2);
			result.put("msg", Constant.ERRORMSG2);
			return json.toJSONString();
		}
		if (dataToken.equals(token)) {
			try {
				String tempString = "2019";
				int j = 123;
				for(int i = 0; i < 50; i++) {
					j -= i;
					tempString += j;
				}
				ssqInfoUtil.getTodaySsqInfo(tempString);
				result.put("status", Constant.SUCESSCODE);
				result.put("msg", Constant.SUCESSMSG);
			} catch (Exception e) {
				result.put("status", Constant.ERRORCODE1);
				result.put("msg", Constant.ERRORMSG1);
				logger.error("makeCPData is exception:" + e.toString());
			}
		} else {
			result.put("status", Constant.ERRORCODE1);
			result.put("msg", Constant.ERRORMSG1);
		}
		return json.toJSONString();
	}

}
