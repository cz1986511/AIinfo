package com.danlu.dleye.client.entity;

import java.util.List;

public class FudaiDetail
{
    private String fdId;

    private String fdName;

    private Long fdAmount;

    private Long fdPrice;

    private String fdCode;

    private String fdType;

    private String fdStatus;

    private Long userId;

    private String createPerson;

    private List<FudaiItemInfo> fudaiItemInfos;

    private List<FudaiPictureInfo> fudaiPictureInfos;

    public String getFdId()
    {
        return fdId;
    }

    public void setFdId(String fdId)
    {
        this.fdId = fdId;
    }

    public String getFdName()
    {
        return fdName;
    }

    public void setFdName(String fdName)
    {
        this.fdName = fdName;
    }

    public Long getFdAmount()
    {
        return fdAmount;
    }

    public void setFdAmount(Long fdAmount)
    {
        this.fdAmount = fdAmount;
    }

    public Long getFdPrice()
    {
        return fdPrice;
    }

    public void setFdPrice(Long fdPrice)
    {
        this.fdPrice = fdPrice;
    }

    public String getFdCode()
    {
        return fdCode;
    }

    public void setFdCode(String fdCode)
    {
        this.fdCode = fdCode;
    }

    public String getFdType()
    {
        return fdType;
    }

    public void setFdType(String fdType)
    {
        this.fdType = fdType;
    }

    public String getFdStatus()
    {
        return fdStatus;
    }

    public void setFdStatus(String fdStatus)
    {
        this.fdStatus = fdStatus;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public String getCreatePerson()
    {
        return createPerson;
    }

    public void setCreatePerson(String createPerson)
    {
        this.createPerson = createPerson;
    }

    public List<FudaiItemInfo> getFudaiItemInfos()
    {
        return fudaiItemInfos;
    }

    public void setFudaiItemInfos(List<FudaiItemInfo> fudaiItemInfos)
    {
        this.fudaiItemInfos = fudaiItemInfos;
    }

    public List<FudaiPictureInfo> getFudaiPictureInfos()
    {
        return fudaiPictureInfos;
    }

    public void setFudaiPictureInfos(List<FudaiPictureInfo> fudaiPictureInfos)
    {
        this.fudaiPictureInfos = fudaiPictureInfos;
    }

}
