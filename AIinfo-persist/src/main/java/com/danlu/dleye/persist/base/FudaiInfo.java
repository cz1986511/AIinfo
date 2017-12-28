package com.danlu.dleye.persist.base;

import java.util.Date;

public class FudaiInfo
{
    private Long id;

    private String fdId;

    private String fdName;

    private Long fdAmount;

    private Long fdPrice;

    private String fdCode;

    private String fdType;

    /**01-新建,02-，03-分享,99-删除*/
    private String fdStatus;

    private Long userId;

    private String createPerson;

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

    public String getFdId()
    {
        return fdId;
    }

    public void setFdId(String fdId)
    {
        this.fdId = fdId == null ? null : fdId.trim();
    }

    public String getFdName()
    {
        return fdName;
    }

    public void setFdName(String fdName)
    {
        this.fdName = fdName == null ? null : fdName.trim();
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
        this.fdCode = fdCode == null ? null : fdCode.trim();
    }

    public String getFdType()
    {
        return fdType;
    }

    public void setFdType(String fdType)
    {
        this.fdType = fdType == null ? null : fdType.trim();
    }

    public String getFdStatus()
    {
        return fdStatus;
    }

    public void setFdStatus(String fdStatus)
    {
        this.fdStatus = fdStatus == null ? null : fdStatus.trim();
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
        this.createPerson = createPerson == null ? null : createPerson.trim();
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