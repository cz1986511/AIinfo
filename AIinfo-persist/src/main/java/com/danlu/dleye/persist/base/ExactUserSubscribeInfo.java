package com.danlu.dleye.persist.base;

import java.util.Date;

public class ExactUserSubscribeInfo {
    private Long id;

    private String userTel;

    private String userSubscribeCode;

    private String userSubscribeName;

    private String status;

    private Date gmtCreate;

    private Date gmtModified;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserTel() {
        return userTel;
    }

    public void setUserTel(String userTel) {
        this.userTel = userTel == null ? null : userTel.trim();
    }

    public String getUserSubscribeCode() {
        return userSubscribeCode;
    }

    public void setUserSubscribeCode(String userSubscribeCode) {
        this.userSubscribeCode = userSubscribeCode == null ? null : userSubscribeCode.trim();
    }

    public String getUserSubscribeName() {
        return userSubscribeName;
    }

    public void setUserSubscribeName(String userSubscribeName) {
        this.userSubscribeName = userSubscribeName == null ? null : userSubscribeName.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
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