package xiaozhuo.info.persist.mapper;

import java.util.List;
import java.util.Map;

import xiaozhuo.info.persist.base.Idea;

public interface IdeaMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Idea idea);

    int insertSelective(Idea idea);

    Idea selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Idea idea);

    int updateByPrimaryKey(Idea idea);
    
    List<Idea> selectIdeasByParams(Map<String, Object> map);
}