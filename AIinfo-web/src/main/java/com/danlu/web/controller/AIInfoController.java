package com.danlu.web.controller;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
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
import com.danlu.dleye.core.UserInfoManager;
import com.danlu.dleye.core.UserSignManager;
import com.danlu.dleye.core.util.DleyeSwith;
import com.danlu.dleye.core.util.RedisClient;
import com.danlu.dleye.persist.base.ArticleInfo;
import com.danlu.dleye.persist.base.UserInfoEntity;
import com.danlu.dleye.persist.base.UserSign;

@SuppressWarnings("deprecation")
@Controller
public class AIInfoController implements Serializable {

    private static final long serialVersionUID = -90859094251L;
    private static Logger logger = LoggerFactory.getLogger(AIInfoController.class);
    @Autowired
    private UserSignManager userSignManager;
    @Autowired
    private UserInfoManager userManager;
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
        List<JSONObject> list = null;
        try {
            list = (List<JSONObject>) redisClient.get(weather,
                new TypeReference<List<JSONObject>>() {
                });
            if (!CollectionUtils.isEmpty(list)) {
                result.put("data", list);
            } else {
                list = new ArrayList<JSONObject>();
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

    @RequestMapping(value = "oilinfo", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getOilInfo(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<String, Object>();
        JSONObject json = new JSONObject(result);
        String oilKey = "oilKey";
        JSONObject oilJson = null;
        try {
            oilJson = (JSONObject) redisClient.get(oilKey, new TypeReference<JSONObject>() {
            });
            if (!CollectionUtils.isEmpty(oilJson)) {
                result.put("data", oilJson);
            } else {
                result.put("status", 1);
                result.put("msg", "程序小哥跟老板娘跑了");
            }
            result.put("status", 0);
        } catch (Exception e) {
            result.put("status", 1);
            result.put("msg", "程序小哥跟老板娘跑了");
            logger.error("getOilInfo is exception:" + e.toString());
        }
        return json.toJSONString();
    }

    @RequestMapping(value = "wx_action", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String siginToken(HttpServletRequest request) {
        String echostr = request.getParameter("echostr");
        return echostr;
    }

    @RequestMapping(value = "wx_action1", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String wxAction(HttpServletRequest request) {
        Document getdocument = null;
        try {
            String toUser = null;
            String fromUser = null;
            String createTime = null;
            String msgType = null;
            String content = null;
            String eventKey = null;
            boolean isEvent = false;
            BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(),
                "UTF-8"));
            String buffer = null;
            StringBuffer xml = new StringBuffer();
            while ((buffer = br.readLine()) != null) {
                xml.append(buffer);
            }
            SAXReader reader = new SAXReader();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(xml.toString().getBytes());
            InputStreamReader ir = new InputStreamReader(inputStream);
            getdocument = reader.read(ir);
            Element parenetElement = getdocument.getRootElement();
            for (Iterator<Element> i = parenetElement.elementIterator(); i.hasNext();) {
                Element nodeElement = i.next();
                if ("ToUserName".equals(nodeElement.getName())) {
                    toUser = nodeElement.getText();
                }
                if ("FromUserName".equals(nodeElement.getName())) {
                    fromUser = nodeElement.getText();
                }
                if ("CreateTime".equals(nodeElement.getName())) {
                    createTime = nodeElement.getText();
                }
                if ("MsgType".equals(nodeElement.getName())) {
                    msgType = nodeElement.getText();
                }
                if ("Content".equals(nodeElement.getName())) {
                    content = nodeElement.getText();
                }
                if ("EventKey".equals(nodeElement.getName())) {
                    eventKey = nodeElement.getText();
                }
                System.out.println(nodeElement.getName() + ":" + nodeElement.getText());
            }
            if ("text".equals(msgType) && null != content) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("openId", fromUser);
                List<UserInfoEntity> list = userManager.getUserListByParams(map);
                if (CollectionUtils.isEmpty(list)) {
                    Map<String, Object> map1 = new HashMap<String, Object>();
                    map.put("tel", content);
                    List<UserInfoEntity> list1 = userManager.getUserListByParams(map1);
                    if (!CollectionUtils.isEmpty(list1)) {
                        UserInfoEntity newUserInfoEntity = new UserInfoEntity();
                        newUserInfoEntity.setUserId(list1.get(0).getUserId());
                        newUserInfoEntity.setOpenId(fromUser);
                        userManager.updateUserInfo(newUserInfoEntity);
                        content = "绑定成功";
                    } else {
                        content = "手机号输入错误";
                    }
                } else {
                    content = "请勿重复绑定";
                }
            } else if ("event".equals(msgType)) {
                isEvent = true;
                Map<String, Object> map2 = new HashMap<String, Object>();
                map2.put("openId", fromUser);
                List<UserInfoEntity> list2 = userManager.getUserListByParams(map2);
                if (!CollectionUtils.isEmpty(list2)) {
                    String userName = list2.get(0).getUserName();
                    String dateString = getDateString();
                    Map<String, Object> map3 = new HashMap<String, Object>();
                    map3.put("userName", userName);
                    map3.put("date", dateString);
                    List<UserSign> list = userSignManager.getUserSignListByParams(map3);
                    if (!CollectionUtils.isEmpty(list)) {
                        content = "请勿重复签到";
                    } else {
                        UserSign userSign = new UserSign();
                        userSign.setSignInfo("微信签到");
                        userSign.setUserName(userName);
                        userSign.setDate(getDateString());
                        userSignManager.addUserSign(userSign);
                        logger.info("user:" + userName + "|sign:微信签到");
                        content = "签到成功";
                    }
                } else {
                    content = "请先绑定手机号";
                }
            }
            for (Iterator<Element> j = parenetElement.elementIterator(); j.hasNext();) {
                Element nodeElement = j.next();
                if ("ToUserName".equals(nodeElement.getName())) {
                    nodeElement.setText(fromUser);
                }
                if ("FromUserName".equals(nodeElement.getName())) {
                    nodeElement.setText(toUser);
                }
                if ("MsgType".equals(nodeElement.getName())) {
                    nodeElement.setText("text");
                }
                if ("Content".equals(nodeElement.getName())) {
                    nodeElement.setText(content);
                }
                if ("Event".equals(nodeElement.getName())) {
                    nodeElement.setName("Content");
                    nodeElement.setText(content);
                }
                if ("EventKey".equals(nodeElement.getName())) {
                    j.remove();
                }
                System.out.println(nodeElement.getName() + ":" + nodeElement.getText());
            }
        } catch (Exception e) {
            logger.error(e.toString());
        }
        return getdocument.asXML();
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
        logger.error("getSuggestion is null");
        return null;
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
        logger.error("getSuggestion is null");
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
        logger.error("getSuggestion is null");
        return null;
    }

    private String getDateString() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(new Date());
    }
}
