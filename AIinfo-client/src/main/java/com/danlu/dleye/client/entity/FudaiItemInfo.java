package com.danlu.dleye.client.entity;

public class FudaiItemInfo
{
    private String fdId;

    private Long fdItemId;

    private String fdItemName;

    private Long fdItemPrice;
    private Long fdItemOriginPrice;
    private Integer fdItemNumber;

    private String fdItemPic;

    public void setFdItemOriginPrice(Long fdItemOriginPrice)
    {
        this.fdItemOriginPrice = fdItemOriginPrice;
    }
    public Long getFdItemOriginPrice()
    {
        return fdItemOriginPrice;
    }
    public String getFdId()
    {
        return fdId;
    }

    public void setFdId(String fdId)
    {
        this.fdId = fdId;
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
        this.fdItemName = fdItemName;
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
    @Override
    public String toString()
    {
        return "{\"fdId\":\"" + fdId + "\",\"fdItemId\":\"" + fdItemId + "\",\"fdItemName\":\""
               + fdItemName + "\",\"fdItemPrice\":\"" + fdItemPrice + "\",\"fdItemOriginPrice\":\""
               + fdItemOriginPrice + "\",\"fdItemNumber\":\"" + fdItemNumber
               + "\",\"fdItemPic\":\"" + fdItemPic + "\"}";
    }

}
