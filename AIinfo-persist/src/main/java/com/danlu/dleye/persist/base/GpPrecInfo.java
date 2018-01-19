package com.danlu.dleye.persist.base;

import java.util.Date;

public class GpPrecInfo
{
    private Long id;

    private String gpCode;

    private String gpName;

    private Date datetime;

    private String gpPrecPrice;

    private String gpOpen;

    private String gpClose;

    private String gpHigh;

    private String gpLow;

    private String gpEx1;

    private String gpEx2;

    private String gpVolume;

    private String gpWp;

    private String gpNp;

    private String gpAmount;

    private String gpSyl;

    private String gpZsz;

    private String gpLtsz;

    private String gpQt;

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

    public String getGpCode()
    {
        return gpCode;
    }

    public void setGpCode(String gpCode)
    {
        this.gpCode = gpCode == null ? null : gpCode.trim();
    }

    public String getGpName()
    {
        return gpName;
    }

    public void setGpName(String gpName)
    {
        this.gpName = gpName == null ? null : gpName.trim();
    }

    public Date getDatetime()
    {
        return datetime;
    }

    public void setDatetime(Date datetime)
    {
        this.datetime = datetime;
    }

    public String getGpPrecPrice()
    {
        return gpPrecPrice;
    }

    public void setGpPrecPrice(String gpPrecPrice)
    {
        this.gpPrecPrice = gpPrecPrice == null ? null : gpPrecPrice.trim();
    }

    public String getGpOpen()
    {
        return gpOpen;
    }

    public void setGpOpen(String gpOpen)
    {
        this.gpOpen = gpOpen == null ? null : gpOpen.trim();
    }

    public String getGpClose()
    {
        return gpClose;
    }

    public void setGpClose(String gpClose)
    {
        this.gpClose = gpClose == null ? null : gpClose.trim();
    }

    public String getGpHigh()
    {
        return gpHigh;
    }

    public void setGpHigh(String gpHigh)
    {
        this.gpHigh = gpHigh == null ? null : gpHigh.trim();
    }

    public String getGpLow()
    {
        return gpLow;
    }

    public void setGpLow(String gpLow)
    {
        this.gpLow = gpLow == null ? null : gpLow.trim();
    }

    public String getGpEx1()
    {
        return gpEx1;
    }

    public void setGpEx1(String gpEx1)
    {
        this.gpEx1 = gpEx1 == null ? null : gpEx1.trim();
    }

    public String getGpEx2()
    {
        return gpEx2;
    }

    public void setGpEx2(String gpEx2)
    {
        this.gpEx2 = gpEx2 == null ? null : gpEx2.trim();
    }

    public String getGpVolume()
    {
        return gpVolume;
    }

    public void setGpVolume(String gpVolume)
    {
        this.gpVolume = gpVolume == null ? null : gpVolume.trim();
    }

    public String getGpWp()
    {
        return gpWp;
    }

    public void setGpWp(String gpWp)
    {
        this.gpWp = gpWp == null ? null : gpWp.trim();
    }

    public String getGpNp()
    {
        return gpNp;
    }

    public void setGpNp(String gpNp)
    {
        this.gpNp = gpNp == null ? null : gpNp.trim();
    }

    public String getGpAmount()
    {
        return gpAmount;
    }

    public void setGpAmount(String gpAmount)
    {
        this.gpAmount = gpAmount == null ? null : gpAmount.trim();
    }

    public String getGpSyl()
    {
        return gpSyl;
    }

    public void setGpSyl(String gpSyl)
    {
        this.gpSyl = gpSyl == null ? null : gpSyl.trim();
    }

    public String getGpZsz()
    {
        return gpZsz;
    }

    public void setGpZsz(String gpZsz)
    {
        this.gpZsz = gpZsz;
    }

    public String getGpLtsz()
    {
        return gpLtsz;
    }

    public void setGpLtsz(String gpLtsz)
    {
        this.gpLtsz = gpLtsz;
    }

    public String getGpQt()
    {
        return gpQt;
    }

    public void setGpQt(String gpQt)
    {
        this.gpQt = gpQt == null ? null : gpQt.trim();
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