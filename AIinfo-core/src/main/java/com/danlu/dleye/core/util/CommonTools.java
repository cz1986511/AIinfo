package com.danlu.dleye.core.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class CommonTools
{

    public static String getDateString()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(new Date());
    }

    public static String getMonthString()
    {
        Calendar calendar = Calendar.getInstance();
        if (calendar.get(Calendar.DAY_OF_MONTH) != 1)
        {
            calendar.add(Calendar.MONTH, 1);
        }
        return calendar.get(Calendar.YEAR) + "-" + calendar.get(Calendar.MONTH);
    }

    public static String generateDbKey()
    {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }

    public static Date getStringDate(String dateString)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try
        {
            return formatter.parse(dateString);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static Date getStringDateToS(String dateString)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddhhmmss");
        try
        {
            return formatter.parse(dateString);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return null;
    }

}
