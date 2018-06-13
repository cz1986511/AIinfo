package com.danlu.dleye.persist.base;

import java.util.Date;

public class FoodRecordStatistics
{
    private Long id;

    private String statisticsTime;

    private String statisticsType;

    private String statisticsDataType;

    private Long statisticsNum;

    private String statisticsUnit;

    private String statisticsData;

    private String status;

    private Date gmtCreate;

    private Date gmtModified;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getStatisticsTime()
    {
        return statisticsTime;
    }

    public void setStatisticsTime(String statisticsTime)
    {
        this.statisticsTime = statisticsTime;
    }

    public String getStatisticsType()
    {
        return statisticsType;
    }

    public void setStatisticsType(String statisticsType)
    {
        this.statisticsType = statisticsType == null ? null : statisticsType.trim();
    }

    public String getStatisticsDataType()
    {
        return statisticsDataType;
    }

    public void setStatisticsDataType(String statisticsDataType)
    {
        this.statisticsDataType = statisticsDataType == null ? null : statisticsDataType.trim();
    }

    public Long getStatisticsNum()
    {
        return statisticsNum;
    }

    public void setStatisticsNum(Long statisticsNum)
    {
        this.statisticsNum = statisticsNum;
    }

    public String getStatisticsUnit()
    {
        return statisticsUnit;
    }

    public void setStatisticsUnit(String statisticsUnit)
    {
        this.statisticsUnit = statisticsUnit == null ? null : statisticsUnit.trim();
    }

    public String getStatisticsData()
    {
        return statisticsData;
    }

    public void setStatisticsData(String statisticsData)
    {
        this.statisticsData = statisticsData;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status == null ? null : status.trim();
    }

    public Date getGmtCreate()
    {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate)
    {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified()
    {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified)
    {
        this.gmtModified = gmtModified;
    }
}