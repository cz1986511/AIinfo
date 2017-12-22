package com.danlu.dleye.persist.base;

import java.util.Date;

public class OrderOperation {
    private Long id;

    private String orderNo;

    private String orderOpType;

    private String orderOpTypeName;

    private String orderOpContent;

    private Date orderOpTime;

    private String orderOpPerson;

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

    public String getOrderOpType() {
        return orderOpType;
    }

    public void setOrderOpType(String orderOpType) {
        this.orderOpType = orderOpType == null ? null : orderOpType.trim();
    }

    public String getOrderOpTypeName() {
        return orderOpTypeName;
    }

    public void setOrderOpTypeName(String orderOpTypeName) {
        this.orderOpTypeName = orderOpTypeName == null ? null : orderOpTypeName.trim();
    }

    public String getOrderOpContent() {
        return orderOpContent;
    }

    public void setOrderOpContent(String orderOpContent) {
        this.orderOpContent = orderOpContent == null ? null : orderOpContent.trim();
    }

    public Date getOrderOpTime() {
        return orderOpTime;
    }

    public void setOrderOpTime(Date orderOpTime) {
        this.orderOpTime = orderOpTime;
    }

    public String getOrderOpPerson() {
        return orderOpPerson;
    }

    public void setOrderOpPerson(String orderOpPerson) {
        this.orderOpPerson = orderOpPerson == null ? null : orderOpPerson.trim();
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