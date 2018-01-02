package com.danlu.web.controller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.danlu.dleye.client.entity.JsonResult;
import com.danlu.dleye.core.UserInfoManager;
import com.danlu.dleye.core.util.DleyeSwith;
import com.danlu.dleye.core.util.RedisClient;
import com.danlu.dleye.persist.base.UserInfoEntity;

@Controller
@RequestMapping("/user")
public class UserController implements Serializable
{

    private static final long serialVersionUID = -90654534251L;

    private static Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserInfoManager userManager;
    @Autowired
    private DleyeSwith dleyeSwith;
    @Autowired
    private RedisClient redisClient;

    @RequestMapping(value = "/login", method = { RequestMethod.POST, RequestMethod.GET })
    public ResponseEntity<JsonResult<String>> doLogin(HttpServletRequest request,
                                                      @RequestBody Map<String, Object> condition)
    {
        log.info("登陆参数,condition={}", condition);
        JsonResult<String> data = new JsonResult<String>();
        try
        {
            List<UserInfoEntity> list = userManager.getUserListByParams(condition);
            if (!CollectionUtils.isEmpty(list))
            {
                UserInfoEntity userInfo = list.get(0);
                int status = userInfo.getStatus();
                if (status > 0)
                {
                    String redUrl = (String) condition.get("redUrl");
                    request.getSession().setAttribute("userId", userInfo.getUserId());
                    request.getSession().setAttribute("userName", userInfo.getUserName());
                    data.setData(redUrl);
                    data.setStatus(0);
                    data.setMsg("登陆成功");
                }
                else
                {
                    data.setStatus(1);
                    data.setMsg("账号被锁定");
                    log.error("账号:" + condition.get("tel") + "|被锁定");
                }
            }
            else
            {
                data.setStatus(1);
                data.setMsg("手机号或者密码错误");
            }
        }
        catch (Exception e)
        {
            log.error("登陆异常：e={}", e);
            data.setStatus(1);
            data.setMsg("系统异常");
        }
        return new ResponseEntity<JsonResult<String>>(data, HttpStatus.OK);
    }

    @RequestMapping(value = "/register", method = { RequestMethod.POST, RequestMethod.GET })
    public ResponseEntity<JsonResult<String>> doRegister(HttpServletRequest request,
                                                         @RequestBody Map<String, Object> condition)
    {
        log.info("注册参数,condition={}", condition);
        JsonResult<String> data = new JsonResult<String>();
        try
        {
            Map<String, Object> map = new HashMap<String, Object>();
            String tel = (String) condition.get("tel");
            String redUrl = (String) condition.get("redUrl");
            map.put("tel", tel);
            List<UserInfoEntity> list = userManager.getUserListByParams(map);
            if (!CollectionUtils.isEmpty(list))
            {
                data.setStatus(1);
                data.setMsg("手机号已注册");
                log.error("手机号:" + condition.get("tel") + "|已注册");
            }
            else
            {
                UserInfoEntity userInfo = new UserInfoEntity();
                userInfo.setTel(tel);
                userInfo.setPassword((String) condition.get("password"));
                userInfo.setUserName((String) condition.get("userName"));
                userInfo.setStatus(1);
                userInfo.setType(2);
                Long result = userManager.addUserInfo(userInfo);
                if (result > 0L)
                {
                    data.setData(redUrl);
                    data.setStatus(0);
                    data.setMsg("注册成功");
                }
                else
                {
                    data.setStatus(1);
                    data.setMsg("注册失败,程序猿小哥和老板娘跑啦");
                }
            }
        }
        catch (Exception e)
        {
            log.error("账号注册异常：e={}", e);
            data.setStatus(1);
            data.setMsg("系统异常");
        }
        return new ResponseEntity<JsonResult<String>>(data, HttpStatus.OK);
    }

}
