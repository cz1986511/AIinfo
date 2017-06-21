package com.danlu.dleye.persist.mapper;

import com.danlu.dleye.persist.base.OilInfo;

public interface OilInfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OilInfo record);

    int insertSelective(OilInfo record);

    OilInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OilInfo record);

    int updateByPrimaryKey(OilInfo record);
}