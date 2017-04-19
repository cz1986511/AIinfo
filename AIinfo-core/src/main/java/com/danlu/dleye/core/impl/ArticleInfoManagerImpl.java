package com.danlu.dleye.core.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.danlu.dleye.core.ArticleInfoManager;
import com.danlu.dleye.persist.base.ArticleInfo;
import com.danlu.dleye.persist.mapper.ArticleInfoMapper;

public class ArticleInfoManagerImpl implements ArticleInfoManager {

    @Autowired
    private ArticleInfoMapper articleInfoMapper;

    @Override
    public int addArticleInfo(ArticleInfo articleInfo) {
        if (null != articleInfo) {
            return articleInfoMapper.insertSelective(articleInfo);
        }
        return 0;
    }

    @Override
    public ArticleInfo getArticleInfoById(Long id) {
        if (null != id) {
            return articleInfoMapper.selectByPrimaryKey(id);
        }
        return null;
    }

    @Override
    public List<ArticleInfo> getArticleInfosByParams(Map<String, Object> map) {
        if (!CollectionUtils.isEmpty(map)) {
            return articleInfoMapper.selectArticlesByParams(map);
        }
        return null;
    }

    @Override
    public int deleteArticleInfoById(Long id) {
        if (null != id && id > 0) {
            return articleInfoMapper.deleteByPrimaryKey(id);
        }
        return 0;
    }

    @Override
    public List<ArticleInfo> getArticleInfosByGmtCreate(Map<String, Object> map) {
        if (!CollectionUtils.isEmpty(map)) {
            return articleInfoMapper.selectArticlesByGmtCreate(map);
        }
        return null;
    }

}
