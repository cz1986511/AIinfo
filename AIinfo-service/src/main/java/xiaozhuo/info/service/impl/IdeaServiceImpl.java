package xiaozhuo.info.service.impl;

import java.util.List;
import java.util.Map;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import com.alibaba.excel.util.CollectionUtils;
import xiaozhuo.info.persist.base.Idea;
import xiaozhuo.info.persist.mapper.IdeaMapper;
import xiaozhuo.info.service.IdeaService;
import javax.annotation.Resource;

@Service
public class IdeaServiceImpl implements IdeaService {
	
	@Resource
	IdeaMapper ideaMapper;

	@Override
	public List<Idea> getIdeas(Map<String, Object> map) {
		if (CollectionUtils.isEmpty(map)) {
			return Lists.newArrayList();
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
