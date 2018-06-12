package com.danlu.dleye.core.util;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.danlu.dleye.core.FoodRecordManager;
import com.danlu.dleye.persist.base.FoodRecord;

public class FoodRecordUtil
{
    private static Logger logger = LoggerFactory.getLogger(FoodRecordUtil.class);

    @Autowired
    private FoodRecordManager foodRecordManager;

    public void statisticsFoodRecord()
    {

    }

    private void statisticsByTime()
    {
        Map<String, Object> map = new HashMap<String, Object>();
        Calendar calendar = Calendar.getInstance();
        // 年
        int nowYear = calendar.get(Calendar.YEAR);
        calendar.set(nowYear, 11, 31, 23, 59, 59);
        map.put("recordTimeEndTime", calendar.getTime());
        calendar.set(nowYear, 0, 1, 0, 0, 0);
        map.put("recordTimeStartTime", calendar.getTime());
        List<FoodRecord> list1 = foodRecordManager.getFoodRecordsByParams(map);

        // 月
        // 日
    }

    private void statisticsByType()
    {

    }

}
