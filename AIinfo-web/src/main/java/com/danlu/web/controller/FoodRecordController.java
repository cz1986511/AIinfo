package com.danlu.web.controller;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.danlu.dleye.core.FoodRecordManager;
import com.danlu.dleye.core.InfoManager;
import com.danlu.dleye.core.UserInfoManager;
import com.danlu.dleye.persist.base.FoodRecord;

@Controller
public class FoodRecordController implements Serializable
{

    private static final long serialVersionUID = -908534251L;

    private static Logger logger = LoggerFactory.getLogger(FoodRecordController.class);

    @Autowired
    private InfoManager infoManager;
    @Autowired
    private UserInfoManager userManager;

    @Autowired
    private FoodRecordManager foodRecordManager;

    @RequestMapping("addfood.html")
    public ModelAndView addFood(HttpServletRequest request)
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

    @RequestMapping("addfood.action")
    @ResponseBody
    public String actionAddFood(HttpServletRequest request, HttpServletResponse response)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        JSONObject json = new JSONObject(result);
        String recordTime = request.getParameter("recordTime");
        String number = request.getParameter("number");
        String type = request.getParameter("type");
        String unit = request.getParameter("unit");
        try
        {
            if (!StringUtils.isBlank(recordTime) && !StringUtils.isBlank(number))
            {
                SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Date recordDate = sim.parse(recordTime);
                FoodRecord foodRecord = new FoodRecord();
                foodRecord.setNumber(Integer.valueOf(number));
                foodRecord.setRecordTime(recordDate);
                foodRecord.setType(type);
                foodRecord.setUnit(unit);
                foodRecord.setStatus("01");
                foodRecord.setCreatePerson((String) request.getSession().getAttribute("userName"));
                int addResult = foodRecordManager.addFoodRecord(foodRecord);
                if (addResult > 0)
                {
                    result.put("status", true);
                    result.put("msg", "add success");
                }
            }
        }
        catch (Exception e)
        {
            logger.error(e.toString());
            result.put("status", false);
            result.put("msg", "add fail");
        }
        return json.toJSONString();
    }

    @RequestMapping("delfoodrecord.action")
    @ResponseBody
    public String delFoodRecord(HttpServletRequest request, HttpServletResponse response)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        JSONObject json = new JSONObject(result);
        String id = request.getParameter("id");
        try
        {
            if (null != id)
            {
                Long recordId = Long.valueOf(id);
                int delResult = foodRecordManager.delFoodRecordById(recordId);
                if (delResult > 0)
                {
                    result.put("status", true);
                    result.put("msg", "del:" + id + " success");
                }
                else
                {
                    result.put("status", false);
                    result.put("msg", "del id:" + id + " fail");
                }
            }
        }
        catch (Exception e)
        {
            logger.error(e.toString());
            result.put("status", false);
            result.put("msg", "del id:" + id + " fail");
        }
        return json.toJSONString();
    }

    @RequestMapping("foodrecordlist")
    @ResponseBody
    public String getFoodRecordList(HttpServletRequest request, HttpServletResponse response)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        JSONObject json = new JSONObject(result);
        String recordTime = request.getParameter("recordTime");
        String number = request.getParameter("number");
        String type = request.getParameter("type");
        String unit = request.getParameter("unit");
        try
        {
            if (!StringUtils.isBlank(recordTime) && !StringUtils.isBlank(number))
            {
                SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date recordDate = sim.parse(recordTime);
                FoodRecord foodRecord = new FoodRecord();
                foodRecord.setNumber(Integer.valueOf(number));
                foodRecord.setRecordTime(recordDate);
                foodRecord.setType(type);
                foodRecord.setUnit(unit);
                foodRecord.setStatus("01");
                foodRecord.setCreatePerson((String) request.getSession().getAttribute("userName"));
                int addResult = foodRecordManager.addFoodRecord(foodRecord);
                if (addResult > 0)
                {
                    result.put("status", true);
                    result.put("msg", "add resord");
                }
            }
        }
        catch (Exception e)
        {
            logger.error(e.toString());
            result.put("status", false);
            result.put("msg", "add fail");
        }
        return json.toJSONString();
    }

}
