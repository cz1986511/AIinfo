package com.danlu.dleye.persist.base;

import java.util.Date;

public class ShopBase
{
    private Long shopId;

    private String shopName;

    private String shopTel;

    private String shopManager;

    private String shopType;

    private String shopDesc;

    private String shopStatus;

    private String shopLongitude;

    private String shopLatitude;

    private String shopAreaCode;

    private String shopAmapCode;

    private Date gmtCreate;

    private Date gmtModified;

    public Long getShopId()
    {
        return shopId;
    }

    public void setShopId(Long shopId)
    {
        this.shopId = shopId;
    }

    public String getShopName()
    {
        return shopName;
    }

    public void setShopName(String shopName)
    {
        this.shopName = shopName == null ? null : shopName.trim();
    }

    public String getShopTel()
    {
        return shopTel;
    }

    public void setShopTel(String shopTel)
    {
        this.shopTel = shopTel == null ? null : shopTel.trim();
    }

    public String getShopManager()
    {
        return shopManager;
    }

    public void setShopManager(String shopManager)
    {
        this.shopManager = shopManager == null ? null : shopManager.trim();
    }

    public String getShopType()
    {
        return shopType;
    }

    public void setShopType(String shopType)
    {
        this.shopType = shopType == null ? null : shopType.trim();
    }

    public String getShopDesc()
    {
        return shopDesc;
    }

    public void setShopDesc(String shopDesc)
    {
        this.shopDesc = shopDesc == null ? null : shopDesc.trim();
    }

    public String getShopStatus()
    {
        return shopStatus;
    }

    public void setShopStatus(String shopStatus)
    {
        this.shopStatus = shopStatus == null ? null : shopStatus.trim();
    }

    public String getShopLongitude()
    {
        return shopLongitude;
    }

    public void setShopLongitude(String shopLongitude)
    {
        this.shopLongitude = shopLongitude == null ? null : shopLongitude.trim();
    }

    public String getShopLatitude()
    {
        return shopLatitude;
    }

    public void setShopLatitude(String shopLatitude)
    {
        this.shopLatitude = shopLatitude == null ? null : shopLatitude.trim();
    }

    public String getShopAreaCode()
    {
        return shopAreaCode;
    }

    public void setShopAreaCode(String shopAreaCode)
    {
        this.shopAreaCode = shopAreaCode == null ? null : shopAreaCode.trim();
    }

    public String getShopAmapCode()
    {
        return shopAmapCode;
    }

    public void setShopAmapCode(String shopAmapCode)
    {
        this.shopAmapCode = shopAmapCode;
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