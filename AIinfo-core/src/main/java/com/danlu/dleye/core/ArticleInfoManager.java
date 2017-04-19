package com.danlu.dleye.core;

import java.util.List;
import java.util.Map;

import com.danlu.dleye.persist.base.ArticleInfo;

public interface ArticleInfoManager {

    int addArticleInfo(ArticleInfo articleInfo);

    ArticleInfo getArticleInfoById(Long id);

    List<ArticleInfo> getArticleInfosByParams(Map<String, Object> map);

    int deleteArticleInfoById(Long id);

    List<ArticleInfo> getArticleInfosByGmtCreate(Map<String, Object> map);

}
