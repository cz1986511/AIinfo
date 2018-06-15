package com.danlu.dleye.core;

import com.danlu.dleye.persist.base.FoodRecordStatistics;

public interface FoodRecordStatisticsManager
{
    int addFoodRecordStatistics(FoodRecordStatistics record);

    int addOrUpdateStatistics(FoodRecordStatistics record);

}
