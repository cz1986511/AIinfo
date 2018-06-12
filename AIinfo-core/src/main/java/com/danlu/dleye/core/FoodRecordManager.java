package com.danlu.dleye.core;

import com.danlu.dleye.persist.base.FoodRecord;

public interface FoodRecordManager
{
    int addFoodRecord(FoodRecord record);

    int delFoodRecordById(Long id);

}
