package com.danlu.web.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.danlu.dleye.core.ExactUserManager;
import com.danlu.dleye.core.UserInfoManager;
import com.danlu.dleye.core.UserSignManager;
import com.danlu.dleye.core.util.DleyeSwith;
import com.danlu.dleye.core.util.RedisClient;
import com.danlu.dleye.persist.base.ExactUserInfo;
import com.danlu.web.base.HttpUtil;

@Controller
@RequestMapping("/exact")
public class ExactDataController implements Serializable {

    private static final long serialVersionUID = -90859094251L;
    private static Logger logger = LoggerFactory.getLogger(ExactDataController.class);
    private static String SECRET = "7d2ff0588993e3e14e9e87dea0580434";
    private static String APPID = "wxe6544f8f04a080bb";
    private static String OAUTH2URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
    private static String USERURL = "http://uc.danlu.com/uc/V1/users/?mobileNumber=";
    private static String COMPANYURL = "http://uc.danlu.com/uc/V1/dlcompany/get_companyinfo";
    @Autowired
    private UserSignManager userSignManager;
    @Autowired
    private UserInfoManager userManager;
    @Autowired
    private DleyeSwith dleyeSwith;
    @Autowired
    private RedisClient redisClient;

    @Autowired
    private ExactUserManager exactUserManager;

    @RequestMapping(value = "/wx_index", produces = "text/html;charset=UTF-8")
    public ModelAndView wxIndex(HttpServletRequest request) {
        ModelAndView m = new ModelAndView();
        m.setViewName("login");
        String code = request.getParameter("code");
        if (!StringUtils.isBlank(code)) {
            try {
                String getOauthUrl = OAUTH2URL.replace("APPID", APPID).replace("SECRET", SECRET)
                    .replace("CODE", code);
                JSONObject userOauth = HttpUtil.httpsRequest(getOauthUrl, "GET", null);
                if (null != userOauth) {
                    String userOpenId = userOauth.getString("openid");
                    if (null != userOpenId) {
                        List<String> userOpenIds = new ArrayList<String>();
                        userOpenIds.add(userOpenId);
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("userOpenIds", userOpenIds);
                        List<ExactUserInfo> exactUserInfos = exactUserManager
                            .getExactUserInfosByParams(map);
                        if (CollectionUtils.isEmpty(exactUserInfos)) {
                            m.addObject("userOpenId", userOpenId);
                            m.setViewName("exactlogin");
                        } else {
                            ExactUserInfo userInfo = exactUserInfos.get(0);
                            m.setViewName("exactindex");
                            m.addObject("userName", userInfo.getUserCompanyName());
                            m.addObject("userType", userInfo.getUserCompanyType());
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("wxIndex is Exception:" + e.toString());
            }
        }
        return m;
    }

    @RequestMapping(value = "/wx_login", produces = "text/html;charset=UTF-8")
    public ModelAndView wxLogin(HttpServletRequest request) {
        ModelAndView m = new ModelAndView();
        m.setViewName("exactlogin");
        String userOpenId = request.getParameter("userOpenId");
        String tel = request.getParameter("tel");
        String password = request.getParameter("password");
        if (StringUtils.isBlank(userOpenId) || StringUtils.isBlank(tel)
            || StringUtils.isBlank(password)) {
            m.addObject("msg", "参数不能为空");
        } else {
            List<String> userOpenIds = new ArrayList<String>();
            userOpenIds.add(userOpenId);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("userOpenIds", userOpenIds);
            List<ExactUserInfo> exactUserInfos = exactUserManager.getExactUserInfosByParams(map);
            if (CollectionUtils.isEmpty(exactUserInfos)) {
                JSONObject userJsonObject = HttpUtil.httpRequest(USERURL + tel, "GET", null);
                if (null != userJsonObject && "0".equals(userJsonObject.getString("status"))) {
                    JSONObject data = (JSONObject) userJsonObject.get("data");
                    JSONArray dataArray = (JSONArray) data.get("data_list");
                    if (null != dataArray) {
                        JSONObject userInfo = (JSONObject) dataArray.get(0);
                        String userId = userInfo.getString("userId");
                        String userPasswd = userInfo.getString("userPasswd");
                        if (password.equals(userPasswd)) {
                            map.clear();
                            List<String> tels = new ArrayList<String>();
                            tels.add(tel);
                            map.put("userTels", tels);
                            map.put("userStatus", "1");
                            List<ExactUserInfo> exactUserInfoList = exactUserManager
                                .getExactUserInfosByParams(map);
                            if (!CollectionUtils.isEmpty(exactUserInfoList)) {
                                m.addObject("userOpenId", userOpenId);
                                m.addObject("tel", tel);
                                m.addObject("msg", "手机号已经绑定过了");
                            } else {
                                List<String> userIds = new ArrayList<String>();
                                userIds.add(userId);
                                String params = JSONObject.toJSONString(userIds);
                                JSONObject companyJsonObject = HttpUtil.httpRequest(COMPANYURL,
                                    "POST", params);
                                if (null != companyJsonObject
                                    && "0".equals(companyJsonObject.get("status"))) {
                                    JSONObject companyData = (JSONObject) companyJsonObject
                                        .get("data");
                                    JSONObject companyInfo = (JSONObject) companyData.get(userId);
                                    if (null != companyInfo) {
                                        ExactUserInfo exactUserInfo = new ExactUserInfo();
                                        exactUserInfo.setUserOpenId(userOpenId);
                                        exactUserInfo.setUserTel(tel);
                                        exactUserInfo.setUserName(userInfo.getString("userName"));
                                        exactUserInfo.setUserCompanyId(companyInfo
                                            .getString("companyId"));
                                        exactUserInfo.setUserCompanyName(companyInfo
                                            .getString("companyName"));
                                        exactUserInfo.setUserCompanyType(companyInfo
                                            .getString("companyType"));
                                        exactUserInfo.setUserStatus("1");
                                        int result = exactUserManager
                                            .addExactUserInfo(exactUserInfo);
                                        if (result > 0) {
                                            m.setViewName("exactindex");
                                            m.addObject("userName",
                                                exactUserInfo.getUserCompanyName());
                                            m.addObject("userTel", exactUserInfo.getUserTel());
                                            m.addObject("userType",
                                                exactUserInfo.getUserCompanyType());
                                        } else {
                                            m.addObject("userOpenId", userOpenId);
                                            m.addObject("tel", tel);
                                            m.addObject("msg", "系统异常请稍后再试");
                                        }
                                    }
                                }
                            }

                        } else {
                            m.addObject("userOpenId", userOpenId);
                            m.addObject("tel", tel);
                            m.addObject("msg", "手机号密码不对");
                        }
                    }
                }
            } else {
                ExactUserInfo userInfo = exactUserInfos.get(0);
                m.setViewName("exactindex");
                m.addObject("userName", userInfo.getUserCompanyName());
                m.addObject("userType", userInfo.getUserCompanyType());
            }
        }
        return m;
    }

    public static void main(String[] args) {
        try {
            String gurl = "http://uc.danlu.com/uc/V1/users/?mobileNumber=17780502327";
            JSONObject responseBodyJson = HttpUtil.httpRequest(gurl, "GET", null);
            JSONObject data = (JSONObject) responseBodyJson.get("data");
            System.out.println(data);
            JSONArray dataArray = (JSONArray) data.get("data_list");
            JSONObject userInfo = (JSONObject) dataArray.get(0);
            String userId = userInfo.getString("userId");
            List<String> userIds = new ArrayList<String>();
            userIds.add(userId);
            System.out.println(userId);
            String params = JSONObject.toJSONString(userIds);
            String purl = "http://uc.danlu.com/uc/V1/dlcompany/get_companyinfo";
            JSONObject responseBodyJson1 = HttpUtil.httpRequest(purl, "POST", params);
            JSONObject companyJsonObject = (JSONObject) responseBodyJson1.get("data");
            System.out.println(companyJsonObject.get(userId));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
