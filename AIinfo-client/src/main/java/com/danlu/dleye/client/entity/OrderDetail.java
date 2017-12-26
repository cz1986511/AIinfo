package com.danlu.dleye.client.entity;

import java.util.List;

public class OrderDetail
{
    private String orderNo;

    private String orderType;

    private String orderStatus;

    private Long orderAmount;

    private Long orderRetail;

    private Long orderDiscount;

    private String payNo;

    private String payType;

    private String payStatus;

    private List<OrderItemInfo> orderItemInfos;

    public String getOrderNo()
    {
        return orderNo;
    }

    public void setOrderNo(String orderNo)
    {
        this.orderNo = orderNo;
    }

    public String getOrderType()
    {
        return orderType;
    }

    public void setOrderType(String orderType)
    {
        this.orderType = orderType;
    }

    public String getOrderStatus()
    {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus)
    {
        this.orderStatus = orderStatus;
    }

    public Long getOrderAmount()
    {
        return orderAmount;
    }

    public void setOrderAmount(Long orderAmount)
    {
        this.orderAmount = orderAmount;
    }

    public Long getOrderRetail()
    {
        return orderRetail;
    }

    public void setOrderRetail(Long orderRetail)
    {
        this.orderRetail = orderRetail;
    }

    public Long getOrderDiscount()
    {
        return orderDiscount;
    }

    public void setOrderDiscount(Long orderDiscount)
    {
        this.orderDiscount = orderDiscount;
    }

    public String getPayNo()
    {
        return payNo;
    }

    public void setPayNo(String payNo)
    {
        this.payNo = payNo;
    }

    public String getPayType()
    {
        return payType;
    }

    public void setPayType(String payType)
    {
        this.payType = payType;
    }

    public String getPayStatus()
    {
        return payStatus;
    }

    public void setPayStatus(String payStatus)
    {
        this.payStatus = payStatus;
    }

    public List<OrderItemInfo> getOrderItemInfos()
    {
        return orderItemInfos;
    }

    public void setOrderItemInfos(List<OrderItemInfo> orderItemInfos)
    {
        this.orderItemInfos = orderItemInfos;
    }

}
