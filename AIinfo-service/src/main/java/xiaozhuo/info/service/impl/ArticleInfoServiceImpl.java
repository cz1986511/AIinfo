package xiaozhuo.info.service.impl;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import xiaozhuo.info.persist.base.ArticleInfo;
import xiaozhuo.info.persist.mapper.ArticleInfoMapper;
import xiaozhuo.info.service.ArticleInfoService;

@Service
public class ArticleInfoServiceImpl implements ArticleInfoService {

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
		return Lists.newArrayList();
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
		return Lists.newArrayList();
	}

}
