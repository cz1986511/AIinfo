package xiaozhuo.info.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.excel.util.CollectionUtils;

import xiaozhuo.info.persist.base.Idea;
import xiaozhuo.info.persist.mapper.IdeaMapper;
import xiaozhuo.info.service.IdeaService;

public class IdeaServiceImpl implements IdeaService {
	
	@Autowired
	IdeaMapper ideaMapper;

	@Override
	public List<Idea> getIdeas(Map<String, Object> map) {
		if (CollectionUtils.isEmpty(map)) {
			return null;
		} else {
			return ideaMapper.selectIdeasByParams(map);
		}
	}

	@Override
	public int addIdea(Idea idea) {
		if (null != idea) {
			return ideaMapper.insertSelective(idea);
		}
		return 0;
	}

}
