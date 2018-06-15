package com.danlu.dleye.core.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
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
        String statisticsTime = "";
        for (int i = 0; i < 2; i++)
        {
            if (i == 0)
            {
                // 年
                statisticsTime = "" + nowYear;
                calendar.set(nowYear, 11, 31, 23, 59, 59);
                map.put("recordTimeEndTime", calendar.getTime());
                calendar.set(nowYear, 0, 1, 0, 0, 0);
                map.put("recordTimeStartTime", calendar.getTime());
                // 总和
                addStatistics(map, "09", statisticsTime);
                // 奶粉
                addStatistics(map, "01", statisticsTime);
                // 母乳
                addStatistics(map, "02", statisticsTime);
                map.clear();
            }
            else if (i == 1)
            {
                // 月
                for (int mon = 0; mon <= nowMonth; mon++)
                {
                    int nowMonth1 = mon + 1;
                    statisticsTime = "" + nowYear + "-" + nowMonth1;
                    calendar.set(nowYear, mon, 31, 23, 59, 59);
                    map.put("recordTimeEndTime", calendar.getTime());
                    calendar.set(nowYear, mon, 1, 0, 0, 0);
                    map.put("recordTimeStartTime", calendar.getTime());
                    // 总和
                    addStatistics(map, "09", statisticsTime);
                    // 奶粉
                    addStatistics(map, "01", statisticsTime);
                    // 母乳
                    addStatistics(map, "02", statisticsTime);
                    map.clear();
                    // 日
                    for (int day = 1; day < 32; day++)
                    {
                        statisticsTime = "" + nowYear + "-" + nowMonth1 + "-" + day;
                        calendar.set(nowYear, mon, day, 23, 59, 59);
                        map.put("recordTimeEndTime", calendar.getTime());
                        calendar.set(nowYear, mon, day, 0, 0, 0);
                        map.put("recordTimeStartTime", calendar.getTime());
                        // 总和
                        addStatistics(map, "09", statisticsTime);
                        // 奶粉
                        addStatistics(map, "01", statisticsTime);
                        // 母乳
                        addStatistics(map, "02", statisticsTime);
                        map.clear();
                    }
                }
            }
        }
        makeYearEchartsData("09");
        makeYearEchartsData("01");
        makeYearEchartsData("02");
    }

    private void makeYearEchartsData(String dataType)
    {
        try
        {
            // 年
            Map<String, Object> map = new HashMap<String, Object>();
            Calendar calendar = Calendar.getInstance();
            int nowYear = calendar.get(Calendar.YEAR);
            int nowMonth = calendar.get(Calendar.MONTH);
            String statisticsTime = "";
            Map<String, Object> result = new HashMap<String, Object>();
            JSONObject json = new JSONObject(result);
            List<Integer> totalNumbers = new ArrayList<Integer>();
            List<Integer> monNumbers = new ArrayList<Integer>();
            Long totalLong = 0L;
            for (int mon = 0; mon <= nowMonth; mon++)
            {
                int nowMonth1 = mon + 1;
                statisticsTime = "" + nowYear + "-" + nowMonth1;
                map.put("statisticsTime", statisticsTime);
                map.put("statisticsDataType", dataType);
                map.put("statisticsType", "01");
                List<FoodRecordStatistics> list = foodRecordStatisticsManager.getStatistics(map);
                if (!CollectionUtils.isEmpty(list))
                {
                    Iterator<FoodRecordStatistics> iterator = list.iterator();
                    int total = 0;
                    while (iterator.hasNext())
                    {
                        total += iterator.next().getStatisticsNum();
                    }
                    monNumbers.add(nowMonth1);
                    totalNumbers.add(total);
                    totalLong += total;
                }
                // 月
                makeMonthEchartsData(nowYear, nowMonth1, dataType);
            }
            result.put("dates", monNumbers);
            result.put("numbers", totalNumbers);
            result.put("status", true);
            FoodRecordStatistics foodRecordStatistics = new FoodRecordStatistics();
            foodRecordStatistics.setStatisticsData(json.toJSONString());
            foodRecordStatistics.setStatisticsNum(totalLong);
            foodRecordStatistics.setStatisticsDataType(dataType);
            foodRecordStatistics.setStatisticsTime("" + nowYear);
            foodRecordStatistics.setStatisticsType("02");
            foodRecordStatistics.setStatisticsUnit("01");
            foodRecordStatistics.setStatus("01");
            int result1 = foodRecordStatisticsManager.addOrUpdateStatistics(foodRecordStatistics);
            if (result1 < 1)
            {
                logger.error("makeYearEchartsData is fail:" + nowYear);
            }
        }
        catch (Exception e)
        {
            logger.error("makeYearEchartsData is Exception:" + e.toString());
        }
    }

    private void makeMonthEchartsData(int nowYear, int nowMonth, String dataType)
    {
        // 月
        Map<String, Object> map = new HashMap<String, Object>();
        String statisticsTime = "";
        Map<String, Object> result = new HashMap<String, Object>();
        JSONObject json = new JSONObject(result);
        List<Integer> totalNumbers = new ArrayList<Integer>();
        List<Integer> dayNumbers = new ArrayList<Integer>();
        Long totalLong = 0L;
        for (int day = 1; day < 32; day++)
        {
            statisticsTime = "" + nowYear + "-" + nowMonth + "-" + day;
            map.put("statisticsTime", statisticsTime);
            map.put("statisticsDataType", dataType);
            map.put("statisticsType", "01");
            List<FoodRecordStatistics> list = foodRecordStatisticsManager.getStatistics(map);
            if (!CollectionUtils.isEmpty(list))
            {
                Iterator<FoodRecordStatistics> iterator = list.iterator();
                int total = 0;
                while (iterator.hasNext())
                {
                    total += iterator.next().getStatisticsNum();
                }
                dayNumbers.add(day);
                totalNumbers.add(total);
                totalLong += total;
            }
            makeDayEchartsData(nowYear, nowMonth, day, dataType);
        }
        result.put("dates", dayNumbers);
        result.put("numbers", totalNumbers);
        result.put("status", true);
        FoodRecordStatistics foodRecordStatistics = new FoodRecordStatistics();
        foodRecordStatistics.setStatisticsData(json.toJSONString());
        foodRecordStatistics.setStatisticsNum(totalLong);
        foodRecordStatistics.setStatisticsDataType(dataType);
        foodRecordStatistics.setStatisticsTime("" + nowYear + "-" + nowMonth);
        foodRecordStatistics.setStatisticsType("02");
        foodRecordStatistics.setStatisticsUnit("01");
        foodRecordStatistics.setStatus("01");
        int result1 = foodRecordStatisticsManager.addOrUpdateStatistics(foodRecordStatistics);
        if (result1 < 1)
        {
            logger.error("makeMonthEchartsData is fail:" + nowYear + "-" + nowMonth);
        }
    }

    private void makeDayEchartsData(int nowYear, int nowMonth, int nowDay, String dataType)
    {
        // 天
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> result = new HashMap<String, Object>();
        JSONObject json = new JSONObject(result);
        List<Integer> totalNumbers = new ArrayList<Integer>();
        List<String> timeNumbers = new ArrayList<String>();
        Long totalLong = 0L;
        Calendar calendar = Calendar.getInstance();
        calendar.set(nowYear, nowMonth, nowDay, 0, 0, 0);
        map.put("recordTimeStartTime", calendar.getTime());
        calendar.set(nowYear, nowMonth, nowDay, 23, 59, 59);
        map.put("recordTimeEndTime", calendar.getTime());
        if (!"09".equals(dataType))
        {
            map.put("type", dataType);
        }
        List<FoodRecord> list = foodRecordManager.getFoodRecordsByParams(map);
        if (!CollectionUtils.isEmpty(list))
        {
            SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Iterator<FoodRecord> ite = list.iterator();
            while (ite.hasNext())
            {
                FoodRecord temp = ite.next();
                totalNumbers.add(temp.getNumber());
                timeNumbers.add(ft.format(temp.getRecordTime()));
                totalLong += temp.getNumber();
            }
        }
        result.put("dates", timeNumbers);
        result.put("numbers", totalNumbers);
        result.put("status", true);
        FoodRecordStatistics foodRecordStatistics = new FoodRecordStatistics();
        foodRecordStatistics.setStatisticsData(json.toJSONString());
        foodRecordStatistics.setStatisticsNum(totalLong);
        foodRecordStatistics.setStatisticsDataType(dataType);
        int nowMonth1 = nowMonth + 1;
        foodRecordStatistics.setStatisticsTime("" + nowYear + "-" + nowMonth1 + "-" + nowDay);
        foodRecordStatistics.setStatisticsType("02");
        foodRecordStatistics.setStatisticsUnit("01");
        foodRecordStatistics.setStatus("01");
        int result1 = foodRecordStatisticsManager.addOrUpdateStatistics(foodRecordStatistics);
        if (result1 < 1)
        {
            logger.error("makeDayEchartsData is fail:" + nowYear + "-" + nowMonth + "-" + nowDay);
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
                foodRecordStatistics.setStatisticsType("01");
                foodRecordStatistics.setStatisticsUnit("01");
                foodRecordStatistics.setStatus("01");
                int result = foodRecordStatisticsManager
                    .addOrUpdateStatistics(foodRecordStatistics);
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
