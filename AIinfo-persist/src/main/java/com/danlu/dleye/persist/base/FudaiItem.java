package com.danlu.dleye.persist.base;

import java.util.Date;

public class FudaiItem
{
    private Long id;

    private String fdId;

    private Long fdItemId;

    private String fdItemName;

    private Long fdItemPrice;
    
    private Long fdItemOriginPrice;

    private Integer fdItemNumber;

    private String fdItemPic;

    private Date gmtCreate;

    private Date gmtModified;

    
    public void setFdItemOriginPrice(Long fdItemOriginPrice)
    {
        this.fdItemOriginPrice = fdItemOriginPrice;
    }
    public Long getFdItemOriginPrice()
    {
        return fdItemOriginPrice;
    }
    
    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getFdId()
    {
        return fdId;
    }

    public void setFdId(String fdId)
    {
        this.fdId = fdId == null ? null : fdId.trim();
    }

    public Long getFdItemId()
    {
        return fdItemId;
    }

    public void setFdItemId(Long fdItemId)
    {
        this.fdItemId = fdItemId;
    }

    public String getFdItemName()
    {
        return fdItemName;
    }

    public void setFdItemName(String fdItemName)
    {
        this.fdItemName = fdItemName == null ? null : fdItemName.trim();
    }

    public Long getFdItemPrice()
    {
        return fdItemPrice;
    }

    public void setFdItemPrice(Long fdItemPrice)
    {
        this.fdItemPrice = fdItemPrice;
    }

    public Integer getFdItemNumber()
    {
        return fdItemNumber;
    }

    public void setFdItemNumber(Integer fdItemNumber)
    {
        this.fdItemNumber = fdItemNumber;
    }

    public String getFdItemPic()
    {
        return fdItemPic;
    }

    public void setFdItemPic(String fdItemPic)
    {
        this.fdItemPic = fdItemPic;
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
    @Override
    public String toString()
    {
        return "{\"id\":\"" + id + "\",\"fdId\":\"" + fdId + "\",\"fdItemId\":\"" + fdItemId
               + "\",\"fdItemName\":\"" + fdItemName + "\",\"fdItemPrice\":\"" + fdItemPrice
               + "\",\"fdItemOriginPrice\":\"" + fdItemOriginPrice + "\",\"fdItemNumber\":\""
               + fdItemNumber + "\",\"fdItemPic\":\"" + fdItemPic + "\",\"gmtCreate\":\""
               + gmtCreate + "\",\"gmtModified\":\"" + gmtModified + "\"}";
    }
    
}