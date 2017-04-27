package com.danlu.web.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.danlu.dleye.core.ArticleInfoManager;
import com.danlu.dleye.core.util.DleyeSwith;
import com.danlu.dleye.core.util.RedisClient;
import com.danlu.dleye.persist.base.ArticleInfo;

@Controller
public class AIInfoController implements Serializable {

    private static final long serialVersionUID = -908534251L;
    private static Logger logger = LoggerFactory.getLogger(AIInfoController.class);
    @Autowired
    private ArticleInfoManager articleInfoManager;
    @Autowired
    private DleyeSwith dleyeSwith;
    @Autowired
    private RedisClient redisClient;

    @RequestMapping(value = "art_list", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getArticleList(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<String, Object>();
        JSONObject json = new JSONObject(result);
        String source = request.getParameter("source");
        String tag = request.getParameter("tag");
        String defaultKey = "dKey";
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            if (!StringUtils.isBlank(source)) {
                map.put("source", source);
                defaultKey += source;
            }
            if (!StringUtils.isBlank(tag)) {
                map.put("tag", tag);
                defaultKey += tag;
            }
            map.put("offset", 0);
            map.put("limit", dleyeSwith.getRequestSize());
            List<ArticleInfo> resultList = (List<ArticleInfo>) redisClient.get(defaultKey,
                new TypeReference<List<ArticleInfo>>() {
                });
            if (!CollectionUtils.isEmpty(resultList)) {
                result.put("data", resultList);
            } else {
                List<ArticleInfo> articleInfoList = articleInfoManager.getArticleInfosByParams(map);
                if (!CollectionUtils.isEmpty(articleInfoList)) {
                    result.put("data", articleInfoList);
                    redisClient.set(defaultKey, articleInfoList, dleyeSwith.getEffectiveTime());
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

    @RequestMapping(value = "weather", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getWeather(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<String, Object>();
        JSONObject json = new JSONObject(result);
        String weather = "weather";
        List<JSONObject> list = new ArrayList<JSONObject>();
        try {
            list = (List<JSONObject>) redisClient.get(weather,
                new TypeReference<List<JSONObject>>() {
                });
            if (!CollectionUtils.isEmpty(list)) {
                result.put("data", list);
            } else {
                String citys = dleyeSwith.getCitys();
                String[] cityStrings = citys.split(",");
                for (int i = 0; i < cityStrings.length; i++) {
                    JSONObject jsonObject = getWeatherNext(cityStrings[i]);
                    jsonObject.put("now", getWeatherNow(cityStrings[i]));
                    jsonObject.put("suggestion", getSuggestion(cityStrings[i]));
                    list.add(jsonObject);
                }
                result.put("data", list);
                redisClient.set(weather, list, 3600);
            }
            result.put("status", 0);
        } catch (Exception e) {
            result.put("status", 1);
            result.put("msg", "程序小哥跟老板娘跑了");
            logger.error("getWeather is exception:" + e.toString());
        }
        return json.toJSONString();
    }

    @SuppressWarnings({ "resource", "deprecation" })
    private JSONObject getSuggestion(String city) {
        HttpClient httpClient = new DefaultHttpClient();
        try {
            String restUrl = "https://api.seniverse.com/v3/life/suggestion.json?key=tl9ml0o784jsrc4h&language=zh-Hans&location=";
            HttpGet getMethod = new HttpGet(restUrl + city);
            HttpResponse response = httpClient.execute(getMethod);
            if (null != response) {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode < 300) {
                    String responseBody = EntityUtils.toString(response.getEntity(), "UTF-8");
                    JSONArray array = (JSONArray) JSON.parseObject(responseBody).get("results");
                    return array.getJSONObject(0).getJSONObject("suggestion");
                }
            }
        } catch (Exception e) {
            logger.error("getSuggestion is exception:" + e.toString());
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return null;
    }

    @SuppressWarnings({ "deprecation", "resource" })
    private JSONObject getWeatherNow(String city) {
        HttpClient httpClient = new DefaultHttpClient();
        try {
            String restUrl = "https://api.seniverse.com/v3/weather/now.json?key=tl9ml0o784jsrc4h&language=zh-Hans&unit=c&location=";
            HttpGet getMethod = new HttpGet(restUrl + city);
            HttpResponse response = httpClient.execute(getMethod);
            if (null != response) {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode < 300) {
                    String responseBody = EntityUtils.toString(response.getEntity(), "UTF-8");
                    JSONArray array = (JSONArray) JSON.parseObject(responseBody).get("results");
                    return array.getJSONObject(0).getJSONObject("now");
                }
            }
        } catch (Exception e) {
            logger.error("getSuggestion is exception:" + e.toString());
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return null;
    }

    @SuppressWarnings({ "deprecation", "resource" })
    private JSONObject getWeatherNext(String city) {
        HttpClient httpClient = new DefaultHttpClient();
        try {
            String restUrl = "https://api.seniverse.com/v3/weather/daily.json?key=tl9ml0o784jsrc4h&language=zh-Hans&unit=c&start=0&days=5&location=";
            HttpGet getMethod = new HttpGet(restUrl + city);
            HttpResponse response = httpClient.execute(getMethod);
            if (null != response) {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode < 300) {
                    String responseBody = EntityUtils.toString(response.getEntity(), "UTF-8");
                    JSONArray array = (JSONArray) JSON.parseObject(responseBody).get("results");
                    return array.getJSONObject(0);
                }
            }
        } catch (Exception e) {
            logger.error("getSuggestion is exception:" + e.toString());
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return null;
    }
}
