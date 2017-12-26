package com.danlu.dleye.persist.base;

import java.util.Date;

public class ShoppingcarInfo
{
    private Long id;

    private String shoppingcarId;

    private String userId;

    private String itemType;

    private Long itemId;

    private String itemName;

    private Long itemPrice;

    private Integer itemNumber;

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

    public String getShoppingcarId()
    {
        return shoppingcarId;
    }

    public void setShoppingcarId(String shoppingcarId)
    {
        this.shoppingcarId = shoppingcarId == null ? null : shoppingcarId.trim();
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getItemType()
    {
        return itemType;
    }

    public void setItemType(String itemType)
    {
        this.itemType = itemType == null ? null : itemType.trim();
    }

    public Long getItemId()
    {
        return itemId;
    }

    public void setItemId(Long itemId)
    {
        this.itemId = itemId;
    }

    public String getItemName()
    {
        return itemName;
    }

    public void setItemName(String itemName)
    {
        this.itemName = itemName;
    }

    public Long getItemPrice()
    {
        return itemPrice;
    }

    public void setItemPrice(Long itemPrice)
    {
        this.itemPrice = itemPrice;
    }

    public Integer getItemNumber()
    {
        return itemNumber;
    }

    public void setItemNumber(Integer itemNumber)
    {
        this.itemNumber = itemNumber;
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