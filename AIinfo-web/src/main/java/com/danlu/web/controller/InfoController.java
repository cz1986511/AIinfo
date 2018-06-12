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
import com.danlu.dleye.core.InfoManager;
import com.danlu.dleye.core.UserInfoManager;
import com.danlu.dleye.persist.base.Info;
import com.danlu.dleye.persist.base.UserInfoEntity;

@Controller
public class InfoController implements Serializable
{

    private static final long serialVersionUID = -908534251L;

    private static Logger logger = LoggerFactory.getLogger(InfoController.class);

    @Autowired
    private InfoManager infoManager;
    @Autowired
    private UserInfoManager userManager;

    @RequestMapping("index.html")
    public ModelAndView index(HttpServletRequest request)
    {
        ModelAndView m = new ModelAndView();
        if (null != request.getSession().getAttribute("type"))
        {
            int userType = (int) request.getSession().getAttribute("type");
            m.addObject("userType", userType);
        }
        else
        {
            m.setViewName("login");
            return m;
        }
        return m;
    }

    @RequestMapping("logout.html")
    public ModelAndView logout(HttpServletRequest request)
    {
        ModelAndView m = new ModelAndView();
        request.getSession().setAttribute("userId", null);
        request.getSession().setAttribute("type", null);
        request.getSession().setAttribute("timeout", null);
        request.getSession().setAttribute("employeeId", null);
        request.getSession().setAttribute("departmentId", null);
        request.getSession().setAttribute("postId", null);
        m.setViewName("login");
        return m;
    }

    @RequestMapping("login.html")
    public ModelAndView login(HttpServletRequest request)
    {
        ModelAndView m = new ModelAndView();
        long userId = 0l;
        if (null != request.getSession().getAttribute("userId"))
        {
            userId = (long) request.getSession().getAttribute("userId");
        }
        if (userId > 0)
        {
            m.addObject("userType", (int) request.getSession().getAttribute("type"));
            m.setViewName("index");
        }
        return m;
    }

    @RequestMapping("load.html")
    public ModelAndView load(HttpServletRequest request, HttpServletResponse response)
    {
        ModelAndView m = new ModelAndView();
        String tel = request.getParameter("tel");
        String password = request.getParameter("password");
        if (!StringUtils.isBlank(tel) && !StringUtils.isBlank(password))
        {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("tel", tel);
            map.put("password", password);
            List<UserInfoEntity> list = userManager.getUserListByParams(map);
            if (!CollectionUtils.isEmpty(list))
            {
                UserInfoEntity user = list.get(0);
                int status = user.getStatus();
                if (status > 0)
                {
                    Long timeout = (60 * 60 * 1000) + System.currentTimeMillis();
                    request.getSession().setAttribute("userId", user.getUserId());
                    request.getSession().setAttribute("userName", user.getUserName());
                    request.getSession().setAttribute("type", user.getType());
                    request.getSession().setAttribute("timeout", timeout);
                    m.addObject("userType", user.getType());
                    m.setViewName("index");
                }
                else
                {
                    m.addObject("errorMsg", "账号被锁定");
                    m.setViewName("login");
                }
            }
            else
            {
                m.addObject("errorMsg", "手机号或者密码错误");
                m.setViewName("login");
            }
        }
        else
        {
            m.setViewName("login");
        }
        return m;
    }

    @RequestMapping("infolist.html")
    public ModelAndView getAllInfo(HttpServletRequest request)
    {
        ModelAndView m = new ModelAndView();
        try
        {
            List<Info> list = infoManager.getAllInfos();
            m.addObject("jobList", list);
        }
        catch (Exception e)
        {
            logger.info(e.toString());
        }
        return m;
    }

    @RequestMapping("switch_list.html")
    public ModelAndView getAllSwitch(HttpServletRequest request)
    {
        ModelAndView m = new ModelAndView();
        String ip = request.getParameter("ip");
        String name = request.getParameter("name");
        String port = request.getParameter("port");
        m.addObject("ip", ip);
        m.addObject("name", name);
        m.addObject("port", port);
        return m;
    }

    @RequestMapping("addinfo.html")
    public ModelAndView addInfo(HttpServletRequest request)
    {
        ModelAndView m = new ModelAndView();
        return m;
    }

    @RequestMapping("addinfoaction.html")
    @ResponseBody
    public String addInfoAction(HttpServletRequest request)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        JSONObject json = new JSONObject(result);
        String ip = request.getParameter("ip");
        String name = request.getParameter("name");
        String port = request.getParameter("port");
        String desc = request.getParameter("desc");
        if (null == ip || null == name || null == port)
        {
            result.put("status", false);
            result.put("msg", "parameter is null");
        }
        try
        {
            Info info = new Info();
            info.setIp(ip);
            info.setName(name);
            info.setPort(Integer.valueOf(port));
            info.setDesc(desc);
            infoManager.addInfo(info);
            result.put("status", true);
        }
        catch (Exception e)
        {
            logger.error(e.toString());
        }
        return json.toJSONString();
    }

    @RequestMapping("deleteinfo.html")
    @ResponseBody
    public String deleteInfo(HttpServletRequest request, HttpServletResponse response)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        JSONObject json = new JSONObject(result);
        String id = request.getParameter("id");
        try
        {
            if (null != id)
            {
                long infoId = Long.valueOf(id);
                infoManager.deleteInfo(infoId);
                result.put("status", true);
            }
        }
        catch (Exception e)
        {
            logger.error(e.toString());
            result.put("status", false);
            result.put("msg", "delete id:" + id + " info failed");
        }
        return json.toJSONString();
    }

}
