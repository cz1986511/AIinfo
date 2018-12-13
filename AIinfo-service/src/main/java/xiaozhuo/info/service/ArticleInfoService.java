package xiaozhuo.info.service;

import java.util.List;
import java.util.Map;

import xiaozhuo.info.persist.base.ArticleInfo;

public interface ArticleInfoService {

    int addArticleInfo(ArticleInfo articleInfo);

    ArticleInfo getArticleInfoById(Long id);

    List<ArticleInfo> getArticleInfosByParams(Map<String, Object> map);

    int deleteArticleInfoById(Long id);

    List<ArticleInfo> getArticleInfosByGmtCreate(Map<String, Object> map);

}
