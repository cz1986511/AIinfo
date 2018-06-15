package com.danlu.dleye.core.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

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

    @Override
    public int addOrUpdateStatistics(FoodRecordStatistics record)
    {
        int result = 0;
        if (null != record)
        {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("statisticsTime", record.getStatisticsTime());
            map.put("statisticsDataType", record.getStatisticsDataType());
            map.put("statisticsType", record.getStatisticsType());
            List<FoodRecordStatistics> list = foodRecordStatisticsMapper.selectByParams(map);
            if (!CollectionUtils.isEmpty(list))
            {
                FoodRecordStatistics temp = list.get(0);
                record.setId(temp.getId());
                result = foodRecordStatisticsMapper.updateByPrimaryKeySelective(record);
            }
            else
            {
                result = foodRecordStatisticsMapper.insertSelective(record);
            }
        }
        return result;
    }

    @Override
    public List<FoodRecordStatistics> getStatistics(Map<String, Object> map)
    {
        if (!CollectionUtils.isEmpty(map))
        {
            return foodRecordStatisticsMapper.selectByParams(map);
        }
        return null;
    }

}
