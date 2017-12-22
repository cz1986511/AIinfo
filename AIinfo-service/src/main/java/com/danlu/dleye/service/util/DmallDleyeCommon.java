package com.danlu.dleye.service.util;

import com.danlu.dleye.annotation.Switch;

public class DmallDleyeCommon
{
    @Switch(description = "店铺搜索展示数量", name = "shopShowNums")
    private int shopShowNums = 10;

    @Switch(description = "用户默认城市amapCode", name = "defaultArea")
    private String defaultArea = "510100";

    @Switch(description = "用户默认城市Longitude", name = "defaultLongitude")
    private String defaultLongitude = "104.06335";

    @Switch(description = "用户默认城市defaultLatitude", name = "defaultLatitude")
    private String defaultLatitude = "30.568662";

    @Switch(description = "默认搜索半径", name = "defaultR")
    private Double defaultR = 0.044916;

    public String getDefaultArea()
    {
        return defaultArea;
    }

    public void setDefaultArea(String defaultArea)
    {
        this.defaultArea = defaultArea;
    }

    public String getDefaultLongitude()
    {
        return defaultLongitude;
    }

    public void setDefaultLongitude(String defaultLongitude)
    {
        this.defaultLongitude = defaultLongitude;
    }

    public String getDefaultLatitude()
    {
        return defaultLatitude;
    }

    public void setDefaultLatitude(String defaultLatitude)
    {
        this.defaultLatitude = defaultLatitude;
    }

    public Double getDefaultR()
    {
        return defaultR;
    }

    public void setDefaultR(Double defaultR)
    {
        this.defaultR = defaultR;
    }

    public int getShopShowNums()
    {
        return shopShowNums;
    }

    public void setShopShowNums(int shopShowNums)
    {
        this.shopShowNums = shopShowNums;
    }

}
