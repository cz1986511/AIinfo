package com.danlu.dleye.core.impl;

import org.springframework.beans.factory.annotation.Autowired;

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

}
