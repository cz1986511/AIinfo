package xiaozhuo.info.service;

import java.util.List;
import java.util.Map;

import xiaozhuo.info.persist.base.Idea;

/**
 * @author chenzhuo
 * @date   2021-04-01
 */
public interface IdeaService {

	/**
	 * idea列表
	 * @param map
	 * @return
	 */
	List<Idea> getIdeas(Map<String, Object> map);

	/**
	 * 新增idea
	 * @param idea
	 * @return
	 */
	int addIdea(Idea idea);

}
