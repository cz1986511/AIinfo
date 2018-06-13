package com.danlu.dleye.core.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.danlu.dleye.core.FoodRecordStatisticsManager;
import com.danlu.dleye.persist.base.FoodRecordStatistics;
import com.danlu.dleye.persist.mapper.FoodRecordStatisticsMapper;

public class FoodRecordStatisticsManagerImpl implements FoodRecordStatisticsManager
{
    @Autowired
    private FoodRecordStatisticsMapper foodRecordStatisticsMapper;

    @Override
    public int addFoodRecordStatistics(FoodRecordStatistics record)
    {
        int result = 0;
        if (null != record)
        {
            result = foodRecordStatisticsMapper.insertSelective(record);
        }
        return result;
    }

}
