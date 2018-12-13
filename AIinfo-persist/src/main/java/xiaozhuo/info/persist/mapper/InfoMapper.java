package xiaozhuo.info.persist.mapper;

import java.util.HashMap;
import java.util.List;

import xiaozhuo.info.persist.base.Info;

public interface InfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Info info);

    int insertSelective(Info info);

    Info selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Info info);

    int updateByPrimaryKey(Info info);
    
    List<Info> selectAllInfos(HashMap<String, Integer> hashMap);
}