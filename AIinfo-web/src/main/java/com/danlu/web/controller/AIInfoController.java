package com.danlu.web.controller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.danlu.dleye.core.ArticleInfoManager;
import com.danlu.dleye.persist.base.ArticleInfo;

@Controller
public class AIInfoController implements Serializable {

    private static final long serialVersionUID = -908534251L;
    private static Logger logger = LoggerFactory.getLogger(AIInfoController.class);
    @Autowired
    private ArticleInfoManager articleInfoManager;

    @RequestMapping("art_list")
    @ResponseBody
    public String getArticleList(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<String, Object>();
        JSONObject json = new JSONObject(result);
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("offset", 0);
            map.put("limit", 50);
            List<ArticleInfo> articleInfoList = articleInfoManager.getArticleInfosByParams(map);
            if (!CollectionUtils.isEmpty(articleInfoList)) {
                result.put("data", articleInfoList);
            }
        } catch (Exception e) {
            logger.error(e.toString());
        }
        return json.toJSONString();
    }

}
