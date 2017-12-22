package com.danlu.dleye.persist.base;

import java.util.Date;

public class OrderItem {
    private Long id;

    private String orderNo;

    private String orderItemType;

    private Long orderItemId;

    private Integer orderItemNumber;

    private Long orderItemPrice;

    private Date gmtCreate;

    private Date gmtModified;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo == null ? null : orderNo.trim();
    }

    public String getOrderItemType() {
        return orderItemType;
    }

    public void setOrderItemType(String orderItemType) {
        this.orderItemType = orderItemType == null ? null : orderItemType.trim();
    }

    public Long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
    }

    public Integer getOrderItemNumber() {
        return orderItemNumber;
    }

    public void setOrderItemNumber(Integer orderItemNumber) {
        this.orderItemNumber = orderItemNumber;
    }

    public Long getOrderItemPrice() {
        return orderItemPrice;
    }

    public void setOrderItemPrice(Long orderItemPrice) {
        this.orderItemPrice = orderItemPrice;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }
}