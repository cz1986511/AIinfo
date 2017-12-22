package com.danlu.dleye.persist.mapper;

import com.danlu.dleye.persist.base.FudaiItem;

public interface FudaiItemMapper
{
    int deleteByPrimaryKey(Long id);

    int insert(FudaiItem record);

    int insertSelective(FudaiItem record);

    FudaiItem selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(FudaiItem record);

    int updateByPrimaryKey(FudaiItem record);
}