package com.danlu.web.controller;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.ConnectException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.danlu.dleye.core.ExactUserManager;
import com.danlu.dleye.core.UserInfoManager;
import com.danlu.dleye.core.UserSignManager;
import com.danlu.dleye.core.util.DleyeSwith;
import com.danlu.dleye.core.util.RedisClient;
import com.danlu.dleye.persist.base.ExactUserInfo;

@Controller
@RequestMapping("/exact")
public class ExactDataController implements Serializable {

    private static final long serialVersionUID = -90859094251L;
    private static Logger logger = LoggerFactory.getLogger(ExactDataController.class);
    private static String SECRET = "7d2ff0588993e3e14e9e87dea0580434";
    private static String APPID = "wxe6544f8f04a080bb";
    private static String OAUTH2URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
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
                JSONObject userOauth = httpsRequest(getOauthUrl, "GET", null);
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
                logger.info("openId:" + userOpenId + "|tel:" + tel + "|password:" + password);
                m.addObject("msg", "登陆成功");
            } else {
                ExactUserInfo userInfo = exactUserInfos.get(0);
                m.setViewName("exactindex");
                m.addObject("userName", userInfo.getUserCompanyName());
                m.addObject("userType", userInfo.getUserCompanyType());
            }
        }
        return m;
    }

    public static JSONObject httpsRequest(String requestUrl, String requestMethod, String outputStr) {
        JSONObject jsonObject = null;
        try {
            X509TrustManager tm = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] arg0, String arg1)
                                                                                   throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] arg0, String arg1)
                                                                                   throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, new TrustManager[] { tm }, new java.security.SecureRandom());
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            URL url = new URL(requestUrl);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setSSLSocketFactory(ssf);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod(requestMethod);
            if (null != outputStr) {
                OutputStream outputStream = conn.getOutputStream();
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;
            StringBuffer buffer = new StringBuffer();
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            inputStream = null;
            conn.disconnect();
            jsonObject = JSONObject.parseObject(buffer.toString());
        } catch (ConnectException ce) {
            logger.error("连接超时：{}", ce);
        } catch (Exception e) {
            logger.error("https请求异常：{}", e);
        }
        return jsonObject;
    }

    public static void main(String[] args) {
        String requestUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
                            + APPID
                            + "&secret="
                            + SECRET
                            + "&code=021vkj5P1M2ha41ztA5P1WUt5P1vkj5M&grant_type=authorization_code";
        JSONObject tempJsonObject = httpsRequest(requestUrl, "GET", null);
        System.out.println(tempJsonObject);
    }

}
