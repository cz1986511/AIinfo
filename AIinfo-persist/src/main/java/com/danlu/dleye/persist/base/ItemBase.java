package com.danlu.dleye.persist.base;

import java.util.Date;

public class ItemBase
{
    private Long itemId;

    private String itemName;

    private String itemInfo;

    private Long itemProductId;

    private String itemCategoryCode;

    private String itemCategoryName;

    private String itemBrandCode;

    private String itemBrandName;

    private Long itemShopId;

    private String itemStatus;

    private String itemDesc;

    private Long itemPrice;

    private Long itemOriginPrice;

    private Integer itemStock;

    private String itemType;

    private Date gmtCreate;

    private Date gmtModified;

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
        this.itemName = itemName == null ? null : itemName.trim();
    }

    public String getItemInfo()
    {
        return itemInfo;
    }

    public void setItemInfo(String itemInfo)
    {
        this.itemInfo = itemInfo == null ? null : itemInfo.trim();
    }

    public Long getItemProductId()
    {
        return itemProductId;
    }

    public void setItemProductId(Long itemProductId)
    {
        this.itemProductId = itemProductId;
    }

    public String getItemCategoryCode()
    {
        return itemCategoryCode;
    }

    public void setItemCategoryCode(String itemCategoryCode)
    {
        this.itemCategoryCode = itemCategoryCode == null ? null : itemCategoryCode.trim();
    }

    public String getItemCategoryName()
    {
        return itemCategoryName;
    }

    public void setItemCategoryName(String itemCategoryName)
    {
        this.itemCategoryName = itemCategoryName == null ? null : itemCategoryName.trim();
    }

    public String getItemBrandCode()
    {
        return itemBrandCode;
    }

    public void setItemBrandCode(String itemBrandCode)
    {
        this.itemBrandCode = itemBrandCode == null ? null : itemBrandCode.trim();
    }

    public String getItemBrandName()
    {
        return itemBrandName;
    }

    public void setItemBrandName(String itemBrandName)
    {
        this.itemBrandName = itemBrandName == null ? null : itemBrandName.trim();
    }

    public Long getItemShopId()
    {
        return itemShopId;
    }

    public void setItemShopId(Long itemShopId)
    {
        this.itemShopId = itemShopId;
    }

    public String getItemStatus()
    {
        return itemStatus;
    }

    public void setItemStatus(String itemStatus)
    {
        this.itemStatus = itemStatus == null ? null : itemStatus.trim();
    }

    public String getItemDesc()
    {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc)
    {
        this.itemDesc = itemDesc == null ? null : itemDesc.trim();
    }

    public Long getItemPrice()
    {
        return itemPrice;
    }

    public void setItemPrice(Long itemPrice)
    {
        this.itemPrice = itemPrice;
    }

    public Long getItemOriginPrice()
    {
        return itemOriginPrice;
    }

    public void setItemOriginPrice(Long itemOriginPrice)
    {
        this.itemOriginPrice = itemOriginPrice;
    }

    public Integer getItemStock()
    {
        return itemStock;
    }

    public void setItemStock(Integer itemStock)
    {
        this.itemStock = itemStock;
    }

    public String getItemType()
    {
        return itemType;
    }

    public void setItemType(String itemType)
    {
        this.itemType = itemType == null ? null : itemType.trim();
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