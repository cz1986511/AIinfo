package com.danlu.dleye.core.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.danlu.dleye.core.FoodRecordManager;
import com.danlu.dleye.persist.base.FoodRecord;
import com.danlu.dleye.persist.mapper.FoodRecordMapper;

public class FoodRecordManagerImpl implements FoodRecordManager
{
    @Autowired
    private FoodRecordMapper foodRecordMapper;

    @Override
    public int addFoodRecord(FoodRecord record)
    {
        int result = 0;
        if (null != record)
        {
            result = foodRecordMapper.insertSelective(record);
        }
        return result;
    }

    @Override
    public int delFoodRecordById(Long id)
    {
        int result = 0;
        if (null != id && id > 0)
        {
            result = foodRecordMapper.deleteByPrimaryKey(id);
        }
        return result;
    }

    @Override
    public List<FoodRecord> getFoodRecordsByParams(Map<String, Object> map)
    {
        if (!CollectionUtils.isEmpty(map))
        {
            return foodRecordMapper.selectFoodRecordsByParams(map);
        }
        return null;
    }

}
