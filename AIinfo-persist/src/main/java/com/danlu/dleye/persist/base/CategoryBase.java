package com.danlu.dleye.persist.base;

import java.util.Date;

public class CategoryBase
{
    private Long categoryId;

    private String categoryCode;

    private String categoryName;

    private String categoryParentCode;

    private Integer categoryLevel;

    private Integer categorySort;

    private String categoryStatus;

    private Date gmtCreate;

    private Date gmtModified;

    public Long getCategoryId()
    {
        return categoryId;
    }

    public void setCategoryId(Long categoryId)
    {
        this.categoryId = categoryId;
    }

    public String getCategoryCode()
    {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode)
    {
        this.categoryCode = categoryCode == null ? null : categoryCode.trim();
    }

    public String getCategoryName()
    {
        return categoryName;
    }

    public void setCategoryName(String categoryName)
    {
        this.categoryName = categoryName == null ? null : categoryName.trim();
    }

    public String getCategoryParentCode()
    {
        return categoryParentCode;
    }

    public void setCategoryParentCode(String categoryParentCode)
    {
        this.categoryParentCode = categoryParentCode == null ? null : categoryParentCode.trim();
    }

    public Integer getCategoryLevel()
    {
        return categoryLevel;
    }

    public void setCategoryLevel(Integer categoryLevel)
    {
        this.categoryLevel = categoryLevel;
    }

    public Integer getCategorySort()
    {
        return categorySort;
    }

    public void setCategorySort(Integer categorySort)
    {
        this.categorySort = categorySort;
    }

    public String getCategoryStatus()
    {
        return categoryStatus;
    }

    public void setCategoryStatus(String categoryStatus)
    {
        this.categoryStatus = categoryStatus == null ? null : categoryStatus.trim();
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