package com.danlu.web.base;

import java.io.Serializable;

public class StatisticsBase implements Serializable
{

    private static final long serialVersionUID = -4738190457389120L;

    private String date; // 统计时间

    private Long number; // 数量

    private String type; // 类型

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public Long getNumber()
    {
        return number;
    }

    public void setNumber(Long number)
    {
        this.number = number;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

}
