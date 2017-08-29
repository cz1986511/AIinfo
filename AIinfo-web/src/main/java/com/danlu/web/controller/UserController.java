package com.danlu.web.controller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.danlu.dleye.core.BookBorrowManager;
import com.danlu.dleye.core.UserInfoManager;
import com.danlu.dleye.core.util.DleyeSwith;
import com.danlu.dleye.core.util.RedisClient;
import com.danlu.dleye.core.util.UserBaseInfo;
import com.danlu.dleye.persist.base.BookBorrow;
import com.danlu.dleye.persist.base.UserInfoEntity;
import com.danlu.persist.util.CommonUtil;

@Controller
public class UserController implements Serializable {

    private static final long serialVersionUID = -90654534251L;

    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserInfoManager userManager;
    @Autowired
    private BookBorrowManager bookBorrowManager;
    @Autowired
    private DleyeSwith dleyeSwith;
    @Autowired
    private RedisClient redisClient;

    @RequestMapping("we_chat.html")
    public ModelAndView chat(HttpServletRequest request) {
        ModelAndView m = new ModelAndView();
        if (null != request.getSession().getAttribute("type")) {
            int userType = (int) request.getSession().getAttribute("type");
            m.addObject("userType", userType);
        } else {
            m.setViewName("login");
            return m;
        }
        return m;
    }

    @RequestMapping("index.html")
    public ModelAndView index(HttpServletRequest request) {
        ModelAndView m = new ModelAndView();
        if (null != request.getSession().getAttribute("type")) {
            int userType = (int) request.getSession().getAttribute("type");
            m.addObject("userType", userType);
            m.addObject("wisdom", dleyeSwith.getWisdom());
        } else {
            m.setViewName("login");
            return m;
        }
        return m;
    }

    @RequestMapping("logout.html")
    public ModelAndView logout(HttpServletRequest request) {
        ModelAndView m = new ModelAndView();
        request.getSession().setAttribute("userId", null);
        request.getSession().setAttribute("type", null);
        request.getSession().setAttribute("timeout", null);
        request.getSession().setAttribute("userName", null);
        m.addObject("wisdom", dleyeSwith.getWisdom());
        m.setViewName("login");
        return m;
    }

    @RequestMapping("login.html")
    public ModelAndView login(HttpServletRequest request) {
        ModelAndView m = new ModelAndView();
        long userId = 0l;
        if (null != request.getSession().getAttribute("userId")) {
            userId = (long) request.getSession().getAttribute("userId");
        }
        if (userId > 0) {
            m.addObject("userType", (int) request.getSession().getAttribute("type"));
            m.setViewName("index");
            m.addObject("userName", (String) request.getSession().getAttribute("userName"));
        }
        m.addObject("wisdom", dleyeSwith.getWisdom());
        return m;
    }

    @RequestMapping("load.html")
    public ModelAndView load(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView m = new ModelAndView();
        String tel = request.getParameter("tel");
        String password = request.getParameter("password");
        if (!StringUtils.isBlank(tel) && !StringUtils.isBlank(password)) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("tel", tel);
            map.put("password", password);
            List<UserInfoEntity> list = userManager.getUserListByParams(map);
            if (!CollectionUtils.isEmpty(list)) {
                UserInfoEntity user = list.get(0);
                int status = user.getStatus();
                if (status > 0) {
                    m.addObject("userType", user.getType());
                    m.setViewName("index");
                    m.addObject("userName", user.getUserName());
                    Long timeout = (dleyeSwith.getTimeout() * 60 * 1000)
                                   + System.currentTimeMillis();
                    request.getSession().setAttribute("userId", user.getUserId());
                    request.getSession().setAttribute("userName", user.getUserName());
                    request.getSession().setAttribute("type", user.getType());
                    request.getSession().setAttribute("timeout", timeout);
                    String keyString = "borrow-" + user.getUserName();
                    Integer remainNum = (Integer) redisClient.get(keyString,
                        new TypeReference<Integer>() {
                        });
                    if (remainNum == null) {
                        remainNum = 1;
                        redisClient.set(keyString, remainNum, 60 * 24 * 60 * 60);
                    }
                } else {
                    m.addObject("msg", "账号被锁定");
                    m.setViewName("login");
                }
            } else {
                m.addObject("msg", "手机号或者密码错误");
                m.setViewName("login");
            }
        } else {
            m.setViewName("login");
        }
        m.addObject("wisdom", dleyeSwith.getWisdom());
        return m;
    }

    @RequestMapping("adduseraction.html")
    @ResponseBody
    public String addInfoAction(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<String, Object>();
        JSONObject json = new JSONObject(result);
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        if (null == email || null == password) {
            result.put("status", false);
            result.put("msg", "parameter is null");
        }
        try {
            result.put("status", true);
        } catch (Exception e) {
            logger.error(e.toString());
        }
        return json.toJSONString();
    }

    @RequestMapping("update_password.html")
    public ModelAndView updatePassword(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView m = new ModelAndView();
        if (null != request.getSession().getAttribute("type")) {
            int userType = (int) request.getSession().getAttribute("type");
            m.addObject("userType", userType);
        } else {
            m.setViewName("login");
            return m;
        }
        return m;
    }

    @RequestMapping("password_update.html")
    @ResponseBody
    public String passwordUpdate(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<String, Object>();
        JSONObject json = new JSONObject(result);
        int res = 0;
        if (null == request.getSession().getAttribute("type")) {
            result.put("msg", "load timeout");
            result.put("status", false);
        } else {
            Long userId = (Long) request.getSession().getAttribute("userId");
            String oldPassword = request.getParameter("oldPassword");
            String newPassword = request.getParameter("newPassword");
            UserInfoEntity userInfoEntity = userManager.getUserInfoByUserId(userId);
            if (null != userInfoEntity) {
                if (CommonUtil.encode(oldPassword, "MD5").equals(userInfoEntity.getPassword())) {
                    UserInfoEntity newUserInfoEntity = new UserInfoEntity();
                    newUserInfoEntity.setUserId(userId);
                    newUserInfoEntity.setPassword(CommonUtil.encode(newPassword, "MD5"));
                    res = userManager.updateUserInfo(newUserInfoEntity);
                } else {
                    result.put("msg", "old password is wrong");
                }
            } else {
                result.put("msg", "user is wrong");
            }
            result.put("status", res > 0 ? true : false);
        }

        return json.toJSONString();
    }

    @RequestMapping("telphone_update.html")
    @ResponseBody
    public String telphoneUpdate(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<String, Object>();
        JSONObject json = new JSONObject(result);
        int res = 0;
        if (null == request.getSession().getAttribute("type")) {
            result.put("msg", "load timeout");
            result.put("status", false);
        } else {
            Long userId = (Long) request.getSession().getAttribute("userId");
            String oldTelphone = request.getParameter("oldTelphone");
            String newTelphone = request.getParameter("newTelphone");
            UserInfoEntity userInfoEntity = userManager.getUserInfoByUserId(userId);
            if (null != userInfoEntity) {
                if (userInfoEntity.getTel() != null && userInfoEntity.getTel().equals(oldTelphone)) {
                    UserInfoEntity newUserInfoEntity = new UserInfoEntity();
                    newUserInfoEntity.setUserId(userId);
                    newUserInfoEntity.setTel(newTelphone);
                    res = userManager.updateUserInfo(newUserInfoEntity);
                } else {
                    result.put("msg", "old telphone is wrong");
                }
            } else {
                result.put("msg", "user is wrong");
            }
            result.put("status", res > 0 ? true : false);
        }
        return json.toJSONString();
    }

    @RequestMapping(value = "user_address_list", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getUserAddressList(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<String, Object>();
        JSONObject json = new JSONObject(result);
        String token = request.getParameter("token");
        String key = "user_address_list";
        if (!StringUtils.isBlank(token) && dleyeSwith.getToken().equals(token)) {
            List<UserBaseInfo> list = (List<UserBaseInfo>) redisClient.get(key,
                new TypeReference<List<UserBaseInfo>>() {
                });
            if (!CollectionUtils.isEmpty(list)) {
                result.put("data", list);
                result.put("status", 0);
            } else {
                result.put("status", 1);
                result.put("msg", "缓存获取数据失败");
            }
        } else {
            result.put("status", 1);
            result.put("msg", "程序猿小哥跟老板娘跑了");
        }
        return json.toJSONString();
    }

    @RequestMapping("userborrow.html")
    public ModelAndView getUserBorrows(HttpServletRequest request) {
        ModelAndView m = new ModelAndView();
        try {
            String userName = (String) request.getSession().getAttribute("userName");
            int userTpye = (int) request.getSession().getAttribute("type");
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("userName", userName);
            List<BookBorrow> resultList = bookBorrowManager.getBookBorrowsByParams(map);
            if (!CollectionUtils.isEmpty(resultList)) {
                int count = resultList.size();
                m.addObject("bookCount", count);
                m.addObject("bookBorrows", resultList);
            }
            m.addObject("userName", userName);
            m.addObject("userType", userTpye);
        } catch (Exception e) {
            logger.info("getUserBorrows is exception:" + e.toString());
        }
        m.addObject("wisdom", dleyeSwith.getWisdom());
        return m;
    }

}
