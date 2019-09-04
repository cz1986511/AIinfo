package xiaozhuo.info.persist.mapper;

import java.util.List;
import java.util.Map;

import xiaozhuo.info.persist.base.TemplateInfo;

public interface TemplateInfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TemplateInfo templateInfo);

    int insertSelective(TemplateInfo templateInfo);

    TemplateInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TemplateInfo templateInfo);

    int updateByPrimaryKey(TemplateInfo templateInfo);
    
    List<TemplateInfo> selectTemplatesByParams(Map<String, Object> map);
}