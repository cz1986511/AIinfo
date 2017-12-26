package com.danlu.dleye.client.entity;

public class OrderItemInfo
{
    private String orderNo;

    private String orderItemType;

    private Long orderItemId;

    private String orderItemName;

    private Integer orderItemNumber;

    private Long orderItemPrice;

    public String getOrderNo()
    {
        return orderNo;
    }

    public void setOrderNo(String orderNo)
    {
        this.orderNo = orderNo;
    }

    public String getOrderItemType()
    {
        return orderItemType;
    }

    public void setOrderItemType(String orderItemType)
    {
        this.orderItemType = orderItemType;
    }

    public Long getOrderItemId()
    {
        return orderItemId;
    }

    public void setOrderItemId(Long orderItemId)
    {
        this.orderItemId = orderItemId;
    }

    public String getOrderItemName()
    {
        return orderItemName;
    }

    public void setOrderItemName(String orderItemName)
    {
        this.orderItemName = orderItemName;
    }

    public Integer getOrderItemNumber()
    {
        return orderItemNumber;
    }

    public void setOrderItemNumber(Integer orderItemNumber)
    {
        this.orderItemNumber = orderItemNumber;
    }

    public Long getOrderItemPrice()
    {
        return orderItemPrice;
    }

    public void setOrderItemPrice(Long orderItemPrice)
    {
        this.orderItemPrice = orderItemPrice;
    }

}
