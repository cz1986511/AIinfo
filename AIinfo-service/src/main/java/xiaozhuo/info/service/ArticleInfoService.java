package xiaozhuo.info.service;

import java.util.List;
import java.util.Map;

import xiaozhuo.info.persist.base.ArticleInfo;

/**
 * @author Chen
 */
public interface ArticleInfoService {

    int addArticleInfo(ArticleInfo articleInfo);

    ArticleInfo getArticleInfoById(Long id);

    List<ArticleInfo> getArticleInfosByParams(Map<String, Object> map);

    int deleteArticleInfoById(Long id);

    List<ArticleInfo> getArticleInfosByGmtCreate(Map<String, Object> map);

    /**
     * 新增文章,如果已经存在就不新增
     */
    int addNewArticle(ArticleInfo articleInfo);

}
