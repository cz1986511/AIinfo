package com.danlu.dleye.core.util;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.danlu.dleye.core.FoodRecordManager;
import com.danlu.dleye.core.FoodRecordStatisticsManager;
import com.danlu.dleye.persist.base.FoodRecord;
import com.danlu.dleye.persist.base.FoodRecordStatistics;

public class FoodRecordUtil
{
    private static Logger logger = LoggerFactory.getLogger(FoodRecordUtil.class);

    @Autowired
    private FoodRecordManager foodRecordManager;
    @Autowired
    private FoodRecordStatisticsManager foodRecordStatisticsManager;

    public void statisticsFoodRecord()
    {
        Map<String, Object> map = new HashMap<String, Object>();
        Calendar calendar = Calendar.getInstance();
        int nowYear = calendar.get(Calendar.YEAR);
        int nowMonth = calendar.get(Calendar.MONTH);
        int nowDay = calendar.get(Calendar.DAY_OF_MONTH);
        String statisticsTime = "";
        for (int i = 0; i < 3; i++)
        {
            if (i == 0)
            {
                // 年
                statisticsTime = "" + nowYear;
                calendar.set(nowYear, 11, 31, 23, 59, 59);
                map.put("recordTimeEndTime", calendar.getTime());
                calendar.set(nowYear, 0, 1, 0, 0, 0);
                map.put("recordTimeStartTime", calendar.getTime());
                map.put("statisticsType", "01");
            }
            else if (i == 1)
            {
                // 月
                int nowMonth1 = nowMonth + 1;
                statisticsTime = "" + nowYear + "-" + nowMonth1;
                calendar.set(nowYear, nowMonth, 31, 23, 59, 59);
                map.put("recordTimeEndTime", calendar.getTime());
                calendar.set(nowYear, nowMonth, 1, 0, 0, 0);
                map.put("recordTimeStartTime", calendar.getTime());
                map.put("statisticsType", "02");
            }
            else if (i == 2)
            {
                // 日
                int nowMonth2 = nowMonth + 1;
                statisticsTime = "" + nowYear + "-" + nowMonth2 + "-" + nowDay;
                calendar.set(nowYear, nowMonth, nowDay, 23, 59, 59);
                map.put("recordTimeEndTime", calendar.getTime());
                calendar.set(nowYear, nowMonth, nowDay, 0, 0, 0);
                map.put("recordTimeStartTime", calendar.getTime());
                map.put("statisticsType", "03");
            }
            // 总和
            addStatistics(map, "09", statisticsTime);
            // 奶粉
            addStatistics(map, "01", statisticsTime);
            // 母乳
            addStatistics(map, "02", statisticsTime);
            map.clear();
        }
    }

    private void addStatistics(Map<String, Object> map, String dataType, String statisticsTime)
    {
        try
        {
            if (!"09".equals(dataType))
            {
                map.put("type", dataType);
            }
            List<FoodRecord> list = foodRecordManager.getFoodRecordsByParams(map);
            if (!CollectionUtils.isEmpty(list))
            {

                Long total = 0L;
                Iterator<FoodRecord> ite1 = list.iterator();
                while (ite1.hasNext())
                {
                    total += ite1.next().getNumber();
                }
                FoodRecordStatistics foodRecordStatistics = new FoodRecordStatistics();
                foodRecordStatistics.setStatisticsDataType(dataType);
                foodRecordStatistics.setStatisticsNum(total);
                foodRecordStatistics.setStatisticsTime(statisticsTime);
                foodRecordStatistics.setStatisticsType((String) map.get("statisticsType"));
                foodRecordStatistics.setStatisticsUnit("01");
                foodRecordStatistics.setStatus("01");
                int result = foodRecordStatisticsManager
                    .addFoodRecordStatistics(foodRecordStatistics);
                if (result <= 0)
                {
                    logger.error("add statistics fail-{statisticsTime:" + statisticsTime
                                 + "|dataType:" + dataType + "|total:" + total + "|type:"
                                 + map.get("statisticsType"));
                }
            }
        }
        catch (Exception e)
        {
            logger.error("addStatistics is Exception:" + e.toString());
        }
    }

}
