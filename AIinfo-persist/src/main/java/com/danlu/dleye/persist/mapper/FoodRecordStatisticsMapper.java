package com.danlu.dleye.persist.mapper;

import java.util.List;
import java.util.Map;

import com.danlu.dleye.persist.base.FoodRecordStatistics;

public interface FoodRecordStatisticsMapper
{
    int deleteByPrimaryKey(Long id);

    int insert(FoodRecordStatistics record);

    int insertSelective(FoodRecordStatistics record);

    FoodRecordStatistics selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(FoodRecordStatistics record);

    int updateByPrimaryKey(FoodRecordStatistics record);

    List<FoodRecordStatistics> selectByParams(Map<String, Object> map);
}