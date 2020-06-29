package xiaozhuo.info.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
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
import xiaozhuo.info.persist.base.Idea;
import xiaozhuo.info.service.ArticleInfoService;
import xiaozhuo.info.service.IdeaService;
import xiaozhuo.info.service.util.Constant;
import xiaozhuo.info.service.util.Crawler;
import xiaozhuo.info.service.util.LimitUtil;
import xiaozhuo.info.service.util.OilInfoUtil;
import xiaozhuo.info.service.util.RedisClient;
import xiaozhuo.info.service.util.SsqInfoUtil;
import xiaozhuo.info.service.util.WeatherUtil;

/**
 * @author zhuochen
 */
@Controller
@RequestMapping("/api")
@Slf4j
public class AIInfoController {

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
	@Autowired
	private IdeaService ideaService;
	
	private static String ART_KEY = "dKey";
	private static String WEATHER_KEY = "weather";
	private static String OIL_KEY = "oilKey";
	private static String IDEA_KEY = "ideaKey";

	private static String STATUS = "status";
	private static String MSG = "msg";
	private static String DATA = "data";

	@Value("${data.token}")
	private String dataToken;

	@RequestMapping(value = "/art/list", method = RequestMethod.POST)
	@ResponseBody
	public String getArticleList(HttpServletRequest request) {
		Map<String, Object> result = new HashMap<>();
		JSONObject json = new JSONObject(result);
		if (!LimitUtil.getRate()) {
			result.put(STATUS, Constant.ERRORCODE2);
			result.put(MSG, Constant.ERRORMSG2);
			return json.toJSONString();
		}
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("offset", 0);
			map.put("limit", 200);
			List<ArticleInfo> resultList = (List<ArticleInfo>) RedisClient.get(ART_KEY,
					new TypeReference<List<ArticleInfo>>() {
					});
			if (!CollectionUtils.isEmpty(resultList)) {
				result.put(DATA, resultList);
			} else {
				List<ArticleInfo> articleInfoList = articleInfoService.getArticleInfosByParams(map);
				if (!CollectionUtils.isEmpty(articleInfoList)) {
					result.put(DATA, articleInfoList);
					RedisClient.set(ART_KEY, articleInfoList, 3600);
				}
			}
			result.put(STATUS, Constant.SUCESSCODE);
		} catch (Exception e) {
			log.error("getArticleList is exception:{}", e.toString());
			result.put(STATUS, Constant.ERRORCODE1);
			result.put(MSG, Constant.ERRORMSG1);
		}
		return json.toJSONString();
	}
	
	@RequestMapping(value = "/idea/list", method = RequestMethod.POST)
	@ResponseBody
	public String getIdeaList(HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		JSONObject json = new JSONObject(result);
		if (!LimitUtil.getRate()) {
			result.put(STATUS, Constant.ERRORCODE2);
			result.put(MSG, Constant.ERRORMSG2);
			return json.toJSONString();
		}
		try {
			
			List<Idea> resultList = (List<Idea>) RedisClient.get(IDEA_KEY,
					new TypeReference<List<Idea>>() {
					});
			if (!CollectionUtils.isEmpty(resultList)) {
				result.put(DATA, resultList);
			} else {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("offset", 0);
				map.put("limit", 200);
				List<Idea> ideaList = ideaService.getIdeas(map);
				if (!CollectionUtils.isEmpty(ideaList)) {
					result.put(DATA, ideaList);
					RedisClient.set(IDEA_KEY, ideaList, 120);
				}
			}
			result.put(STATUS, Constant.SUCESSCODE);
		} catch (Exception e) {
			log.error("getIdeaList is exception:{}", e.toString());
			result.put(STATUS, Constant.ERRORCODE1);
			result.put(MSG, Constant.ERRORMSG1);
		}
		return json.toJSONString();
	}

	@RequestMapping(value = "/weather", produces = "text/html;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String getWeather(HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		JSONObject json = new JSONObject(result);
		if (!LimitUtil.getRate()) {
			result.put(STATUS, Constant.ERRORCODE2);
			result.put(MSG, Constant.ERRORMSG2);
			return json.toJSONString();
		}
		List<JSONObject> list = null;
		try {
			list = (List<JSONObject>) RedisClient.get(WEATHER_KEY, new TypeReference<List<JSONObject>>() {
			});
			if (!CollectionUtils.isEmpty(list)) {
				result.put(DATA, list);
			} else {
				result.put(DATA, weatherUtil.getTodayWeatherInfo());
			}
			result.put(STATUS, Constant.SUCESSCODE);
		} catch (Exception e) {
			result.put(STATUS, Constant.ERRORCODE1);
			result.put(MSG, Constant.ERRORMSG1);
			log.error("getWeather is exception:{}", e.toString());
		}
		return json.toJSONString();
	}

	@RequestMapping(value = "/oilinfo", produces = "text/html;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String getOilInfo(HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		JSONObject json = new JSONObject(result);
		if (!LimitUtil.getRate()) {
			result.put(STATUS, Constant.ERRORCODE2);
			result.put(MSG, Constant.ERRORMSG2);
			return json.toJSONString();
		}
		try {
			JSONObject oilJson = (JSONObject) RedisClient.get(OIL_KEY, new TypeReference<JSONObject>() {
			});
			if (null != oilJson) {
				result.put(STATUS, Constant.SUCESSCODE);
				result.put(DATA, oilJson);
			} else {
				result.put(STATUS, Constant.ERRORCODE1);
				result.put(MSG, Constant.ERRORMSG1);
			}
		} catch (Exception e) {
			result.put(STATUS, Constant.ERRORCODE1);
			result.put(MSG, Constant.ERRORMSG1);
			log.error("getOilInfo is exception:{}", e.toString());
		}
		return json.toJSONString();
	}

	@RequestMapping(value = "/data", produces = "text/html;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String makeData(HttpServletRequest request) {
		String token = request.getParameter("token");
		Map<String, Object> result = new HashMap<String, Object>();
		JSONObject json = new JSONObject(result);
		if (!LimitUtil.getRate()) {
			result.put(STATUS, Constant.ERRORCODE2);
			result.put(MSG, Constant.ERRORMSG2);
			return json.toJSONString();
		}
		if (dataToken.equals(token)) {
			try {
				crawler.crawlerInfo();
				oilInfoUtil.getTodayOilInfo();
				weatherUtil.getTodayWeatherInfo();
				result.put(STATUS, Constant.SUCESSCODE);
				result.put(MSG, Constant.SUCESSMSG);
			} catch (Exception e) {
				result.put(STATUS, Constant.ERRORCODE1);
				result.put(MSG, Constant.ERRORMSG1);
				log.error("makeData is exception:{}", e.toString());
			}
		} else {
			result.put(STATUS, Constant.ERRORCODE1);
			result.put(MSG, Constant.ERRORMSG1);
		}
		return json.toJSONString();
	}

	@RequestMapping(value = "/data/cp", produces = "text/html;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String makeCPData(HttpServletRequest request) {
		String token = request.getParameter("token");
		String qid = request.getParameter("qid");
		Map<String, Object> result = new HashMap<String, Object>();
		JSONObject json = new JSONObject(result);
		if (!LimitUtil.getRate()) {
			result.put(STATUS, Constant.ERRORCODE2);
			result.put(MSG, Constant.ERRORMSG2);
			return json.toJSONString();
		}
		if (dataToken.equals(token)) {
			try {
				ssqInfoUtil.getTodaySsqInfo(qid);
				result.put(STATUS, Constant.SUCESSCODE);
				result.put(MSG, Constant.SUCESSMSG);
			} catch (Exception e) {
				result.put(STATUS, Constant.ERRORCODE1);
				result.put(MSG, Constant.ERRORMSG1);
				log.error("makeCPData is exception:{}", e.toString());
			}
		} else {
			result.put(STATUS, Constant.ERRORCODE1);
			result.put(MSG, Constant.ERRORMSG1);
		}
		return json.toJSONString();
	}

}
