package xiaozhuo.info.persist.mapper;

import java.util.List;
import java.util.Map;

import xiaozhuo.info.persist.base.Idea;

/**
 * @author chenzhuo
 * @date   2021-04-01
 */
public interface IdeaMapper {
    int deleteByPrimaryKey(Long id);

    int insertSelective(Idea idea);

    Idea selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Idea idea);
    
    List<Idea> selectIdeasByParams(Map<String, Object> map);
}