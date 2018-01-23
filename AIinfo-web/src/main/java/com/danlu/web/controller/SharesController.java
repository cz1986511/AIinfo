package com.danlu.web.controller;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.danlu.dleye.core.util.DleyeSwith;
import com.danlu.dleye.core.util.RedisClient;
import com.danlu.dleye.persist.base.GpHisInfo;
import com.danlu.dleye.service.SharesService;
import com.danlu.dleye.util.CalendarUtil;

@Controller
@RequestMapping("/shares")
public class SharesController implements Serializable
{

    private static final long serialVersionUID = -90859094251L;
    private static Logger logger = LoggerFactory.getLogger(SharesController.class);
    @Autowired
    private SharesService sharesService;
    @Autowired
    private DleyeSwith dleyeSwith;
    @Autowired
    private RedisClient redisClient;

    @RequestMapping(value = "/hisInfo", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getSharesHisInfo(HttpServletRequest request)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        JSONObject json = new JSONObject(result);
        String gpCode = request.getParameter("gpCode");
        try
        {
            Map<String, Object> map = new HashMap<String, Object>();
            if (!StringUtils.isBlank(gpCode))
            {
                List<String> gpCodes = new ArrayList<String>();
                gpCodes.add(gpCode);
                map.put("gpCodes", gpCodes);
                List<GpHisInfo> hisInfos = sharesService.getGpHisInfos(map);
                if (!CollectionUtils.isEmpty(hisInfos))
                {
                    Iterator<GpHisInfo> ite = hisInfos.iterator();
                    JSONArray dataArray = new JSONArray();
                    while (ite.hasNext())
                    {
                        GpHisInfo temp = ite.next();
                        JSONArray detailArray = new JSONArray();
                        detailArray.add(CalendarUtil.toString(temp.getDate(), "yyyy-MM-dd"));
                        detailArray.add(Double.valueOf(temp.getGpOpen()));
                        detailArray.add(Double.valueOf(temp.getGpClose()));
                        detailArray.add(Double.valueOf(temp.getGpLow()));
                        detailArray.add(Double.valueOf(temp.getGpHigh()));
                        detailArray.add(Double.valueOf(temp.getGpVolume()));
                        dataArray.add(detailArray);
                    }
                    result.put("data", dataArray);
                }
                result.put("status", 0);
            }
            else
            {
                result.put("status", 1);
            }
        }
        catch (Exception e)
        {
            logger.error("getSharesHisInfo is exception:" + e.toString());
            result.put("status", 1);
            result.put("msg", "程序小哥跟老板娘跑了");
        }
        return json.toJSONString();
    }

    public static void main(String[] args)
    {
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String test = ft.format(new Date());
        System.out.println(test);
    }

}
