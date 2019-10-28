package xiaozhuo.info.persist.mapper;

import java.util.List;
import java.util.Map;

import xiaozhuo.info.persist.base.SsqInfo;

public interface SsqInfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SsqInfo ssqInfo);

    int insertSelective(SsqInfo ssqInfo);

    SsqInfo selectByPrimaryKey(Long id);
    
    SsqInfo selectByQid(Integer qid);
    
    List<SsqInfo> selectByParams(Map<String, Object> map);

    int updateByPrimaryKeySelective(SsqInfo ssqInfo);

    int updateByPrimaryKey(SsqInfo ssqInfo);
}