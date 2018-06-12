package com.danlu.dleye.persist.mapper;

import java.util.List;
import java.util.Map;

import com.danlu.dleye.persist.base.FoodRecord;

public interface FoodRecordMapper
{
    int deleteByPrimaryKey(Long id);

    int insert(FoodRecord record);

    int insertSelective(FoodRecord record);

    FoodRecord selectById(Long id);

    int updateByPrimaryKeySelective(FoodRecord record);

    int updateByPrimaryKey(FoodRecord record);

    List<FoodRecord> selectFoodRecordsByParams(Map<String, Object> map);
}