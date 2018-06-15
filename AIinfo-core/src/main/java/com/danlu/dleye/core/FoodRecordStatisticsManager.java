package com.danlu.dleye.core;

import java.util.List;
import java.util.Map;

import com.danlu.dleye.persist.base.FoodRecordStatistics;

public interface FoodRecordStatisticsManager
{
    int addFoodRecordStatistics(FoodRecordStatistics record);

    int addOrUpdateStatistics(FoodRecordStatistics record);

    List<FoodRecordStatistics> getStatistics(Map<String, Object> map);

}
