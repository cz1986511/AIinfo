package com.danlu.dleye.persist.mapper;

import com.danlu.dleye.persist.base.FoodRecord;

public interface FoodRecordMapper
{
    int deleteByPrimaryKey(Long id);

    int insert(FoodRecord record);

    int insertSelective(FoodRecord record);

    FoodRecord selectById(Long id);

    int updateByPrimaryKeySelective(FoodRecord record);

    int updateByPrimaryKey(FoodRecord record);
}