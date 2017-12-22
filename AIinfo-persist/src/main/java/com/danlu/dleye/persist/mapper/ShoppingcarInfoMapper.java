package com.danlu.dleye.persist.mapper;

import com.danlu.dleye.persist.base.ShoppingcarInfo;

public interface ShoppingcarInfoMapper
{
    int deleteByPrimaryKey(Long id);

    int insert(ShoppingcarInfo record);

    int insertSelective(ShoppingcarInfo record);

    ShoppingcarInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ShoppingcarInfo record);

    int updateByPrimaryKey(ShoppingcarInfo record);
}