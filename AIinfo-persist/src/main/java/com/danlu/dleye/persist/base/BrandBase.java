package com.danlu.dleye.persist.base;

import java.util.Date;

public class BrandBase
{
    private Long brandId;

    private String brandCode;

    private String brandName;

    private String brandLogo;

    private Integer brandSort;

    private String brandStatus;

    private Date gmtCreate;

    private Date gmtModified;

    public Long getBrandId()
    {
        return brandId;
    }

    public void setBrandId(Long brandId)
    {
        this.brandId = brandId;
    }

    public String getBrandCode()
    {
        return brandCode;
    }

    public void setBrandCode(String brandCode)
    {
        this.brandCode = brandCode == null ? null : brandCode.trim();
    }

    public String getBrandName()
    {
        return brandName;
    }

    public void setBrandName(String brandName)
    {
        this.brandName = brandName == null ? null : brandName.trim();
    }

    public String getBrandLogo()
    {
        return brandLogo;
    }

    public void setBrandLogo(String brandLogo)
    {
        this.brandLogo = brandLogo == null ? null : brandLogo.trim();
    }

    public Integer getBrandSort()
    {
        return brandSort;
    }

    public void setBrandSort(Integer brandSort)
    {
        this.brandSort = brandSort;
    }

    public String getBrandStatus()
    {
        return brandStatus;
    }

    public void setBrandStatus(String brandStatus)
    {
        this.brandStatus = brandStatus == null ? null : brandStatus.trim();
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