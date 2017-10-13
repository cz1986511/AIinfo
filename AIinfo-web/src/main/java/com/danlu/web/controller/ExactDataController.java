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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
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
    //微信密码
    private static String SECRET = "7d2ff0588993e3e14e9e87dea0580434";
    //微信appId
    private static String APPID = "wxe6544f8f04a080bb";
    //新订单消息模板ID
    private static String ORDERTEMPLATID = "EpFBzPzTAekeG3E72vCsYerKOA0j-GAyKILvQKlJ2DY";
    //销售统计消息模板ID
    private static String SALLTEMPLATID = "HCXZE9J3Nv8kb0gp_BxLiaHzbYPRPEM0G_ISmNLxsY0";

    //获取用户微信openID地址
    private static String OAUTH2URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
    //获取微信access_token地址
    private static String ACCESSTOKENURL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=SECRET";
    //获取用户微信信息地址
    private static String USERINFOURL = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
    //发送微信模板消息地址
    private static String SENDMSGURL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";

    //丹露用户登录验证地址
    private static String USERURL = "http://uc.danlu.com/uc/V1/users/?mobileNumber=";
    //丹露用户获取企业信息地址
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

    @RequestMapping(value = "/sendwxmsg", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String sendWXMsg(HttpServletRequest request) {
        String openId = request.getParameter("openId");
        if (!StringUtils.isBlank(openId)) {
            sendOrderInfo(openId);
        }
        return null;
    }

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
                                    && "0".equals(companyJsonObject.getString("status"))) {
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

    /**
     * @ 获取微信access_token
     * 
     */
    private String getAccessToken() {
        String getAccessTokenUrl = ACCESSTOKENURL.replace("APPID", APPID).replace("SECRET", SECRET);
        JSONObject accessJsonObject = HttpUtil.httpsRequest(getAccessTokenUrl, "GET", null);
        if (null != accessJsonObject && null != accessJsonObject.getString("access_token")) {
            String access_token = accessJsonObject.getString("access_token");
            redisClient.set("access_token", access_token, 3600);
            return access_token;
        }
        return null;
    }

    /**
     * @ 通过openId获取用户信息
     * @param
     *   openId:用户openId
     */
    private JSONObject getUserWXinfoByOpenId(String openId) {
        JSONObject jsonObject = null;
        if (!StringUtils.isBlank(openId)) {
            try {
                String access_token = redisClient.get("access_token", new TypeReference<String>() {
                });
                if (StringUtils.isBlank(access_token)) {
                    access_token = getAccessToken();
                }
                String getUserInfoUrl = USERINFOURL.replace("ACCESS_TOKEN", access_token).replace(
                    "OPENID", openId);
                jsonObject = HttpUtil.httpsRequest(getUserInfoUrl, "GET", null);
            } catch (Exception e) {
                logger.error("getUserWXinfoByOpenId is Exception:" + e.toString());
            }
        }
        return jsonObject;
    }

    /**
     * @ 发送模板消息
     */
    private JSONObject sendWechatmsgToUser(String jsonString) {
        JSONObject jsonObject = null;
        if (!StringUtils.isBlank(jsonString)) {
            try {
                String access_token = redisClient.get("access_token", new TypeReference<String>() {
                });
                if (StringUtils.isBlank(access_token)) {
                    access_token = getAccessToken();
                }
                String sendUrl = SENDMSGURL.replace("ACCESS_TOKEN", access_token);
                jsonObject = HttpUtil.httpsRequest(sendUrl, "POST", jsonString);
            } catch (Exception e) {
                logger.error("sendWechatmsgToUser is Exception:" + e.toString());
            }
        }
        return jsonObject;
    }

    private void sendOrderInfo(String openId) {
        JSONObject json = new JSONObject();
        json.put("touser", openId);
        json.put("template_id", ORDERTEMPLATID);
        json.put("url", "http://xiaozhuo.info");
        JSONObject data = new JSONObject();
        JSONObject first = new JSONObject();
        first.put("value", "亲,您有新的订单啦");
        first.put("color", "#173177");
        data.put("first", first);
        JSONObject keyword1 = new JSONObject();
        keyword1.put("value", "2017081109");
        keyword1.put("color", "#173177");
        data.put("keyword1", keyword1);
        JSONObject keyword2 = new JSONObject();
        keyword2.put("value", "茅台王子酒");
        keyword2.put("color", "#173177");
        data.put("keyword2", keyword2);
        JSONObject keyword3 = new JSONObject();
        keyword3.put("value", "5瓶");
        keyword3.put("color", "#173177");
        data.put("keyword3", keyword3);
        JSONObject keyword4 = new JSONObject();
        keyword4.put("value", "7000.00元");
        keyword4.put("color", "#173177");
        data.put("keyword4", keyword4);
        JSONObject keyword5 = new JSONObject();
        keyword5.put("value", "货到付款");
        keyword5.put("color", "#173177");
        data.put("keyword5", keyword5);
        JSONObject remark = new JSONObject();
        remark.put("value", "点击查看详情");
        remark.put("color", "#173177");
        data.put("remark", remark);
        json.put("data", data);
        logger.info("json:" + json);
        sendWechatmsgToUser(json.toString());
    }

    public static void main(String[] args) {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
