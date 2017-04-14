package com.danlu.web.controller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.danlu.dleye.core.InfoManager;
import com.danlu.dleye.persist.base.Info;

@Controller
public class InfoController implements Serializable {

    private static final long serialVersionUID = -908534251L;

    private static Logger logger = LoggerFactory.getLogger(InfoController.class);

    private InfoManager infoManager;

    @Autowired
    public void setInfoManager(InfoManager infoManager) {
        this.infoManager = infoManager;
    }

    @RequestMapping("infolist.html")
    public ModelAndView getAllInfo(HttpServletRequest request) {
        ModelAndView m = new ModelAndView();
        try {
            List<Info> list = infoManager.getAllInfos();
            m.addObject("jobList", list);
        } catch (Exception e) {
            logger.info(e.toString());
        }
        return m;
    }

    @RequestMapping("switch_list.html")
    public ModelAndView getAllSwitch(HttpServletRequest request) {
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
    public ModelAndView addInfo(HttpServletRequest request) {
        ModelAndView m = new ModelAndView();
        return m;
    }

    @RequestMapping("addinfoaction.html")
    @ResponseBody
    public String addInfoAction(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<String, Object>();
        JSONObject json = new JSONObject(result);
        String ip = request.getParameter("ip");
        String name = request.getParameter("name");
        String port = request.getParameter("port");
        String desc = request.getParameter("desc");
        if (null == ip || null == name || null == port) {
            result.put("status", false);
            result.put("msg", "parameter is null");
        }
        try {
            Info info = new Info();
            info.setIp(ip);
            info.setName(name);
            info.setPort(Integer.valueOf(port));
            info.setDesc(desc);
            infoManager.addInfo(info);
            result.put("status", true);
        } catch (Exception e) {
            logger.error(e.toString());
        }
        return json.toJSONString();
    }

    @RequestMapping("deleteinfo.html")
    @ResponseBody
    public String deleteInfo(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<String, Object>();
        JSONObject json = new JSONObject(result);
        String id = request.getParameter("id");
        try {
            if (null != id) {
                long infoId = Long.valueOf(id);
                infoManager.deleteInfo(infoId);
                result.put("status", true);
            }
        } catch (Exception e) {
            logger.error(e.toString());
            result.put("status", false);
            result.put("msg", "delete id:" + id + " info failed");
        }
        return json.toJSONString();
    }

}
