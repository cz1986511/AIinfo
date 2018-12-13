package xiaozhuo.info.persist.mapper;

import xiaozhuo.info.persist.base.OilInfo;

public interface OilInfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OilInfo record);

    int insertSelective(OilInfo record);

    OilInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OilInfo record);

    int updateByPrimaryKey(OilInfo record);
}