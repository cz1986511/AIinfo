package com.danlu.dleye.core.util;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.danlu.dleye.core.ArticleInfoManager;
import com.danlu.dleye.persist.base.ArticleInfo;

public class CleanData {

    private static Logger logger = LoggerFactory.getLogger(CleanData.class);
    @Autowired
    private ArticleInfoManager articleInfoManager;
    @Autowired
    private DleyeSwith dleyeSwith;

    public void cleanData() {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, dleyeSwith.getDefaultDate());
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("createTime", calendar.getTime());
            List<ArticleInfo> resultArticleInfoList = articleInfoManager
                .getArticleInfosByGmtCreate(map);
            if (!CollectionUtils.isEmpty(resultArticleInfoList)) {
                Iterator<ArticleInfo> iterator = resultArticleInfoList.iterator();
                while (iterator.hasNext()) {
                    articleInfoManager.deleteArticleInfoById(iterator.next().getId());
                }
            }
        } catch (Exception e) {
            logger.error("cleanData is exception:" + e.toString());
        }
    }

}
