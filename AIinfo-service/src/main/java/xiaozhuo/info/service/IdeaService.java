package xiaozhuo.info.service;

import java.util.List;
import java.util.Map;

import xiaozhuo.info.persist.base.Idea;

public interface IdeaService {
	
	List<Idea> getIdeas(Map<String, Object> map);
	
	int addIdea(Idea idea);

}
