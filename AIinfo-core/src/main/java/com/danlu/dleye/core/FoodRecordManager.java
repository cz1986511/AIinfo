package com.danlu.dleye.core;

import java.util.List;
import java.util.Map;

import com.danlu.dleye.persist.base.FoodRecord;

public interface FoodRecordManager
{
    int addFoodRecord(FoodRecord record);

    int delFoodRecordById(Long id);

    List<FoodRecord> getFoodRecordsByParams(Map<String, Object> map);

}
