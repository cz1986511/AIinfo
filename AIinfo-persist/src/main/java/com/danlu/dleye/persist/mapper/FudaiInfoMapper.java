package com.danlu.dleye.persist.mapper;

import com.danlu.dleye.persist.base.FudaiInfo;

public interface FudaiInfoMapper
{
    int deleteByPrimaryKey(Long id);

    int insert(FudaiInfo record);

    int insertSelective(FudaiInfo record);

    FudaiInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(FudaiInfo record);

    int updateByPrimaryKey(FudaiInfo record);
}