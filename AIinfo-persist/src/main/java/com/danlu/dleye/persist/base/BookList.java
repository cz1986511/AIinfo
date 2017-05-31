package com.danlu.dleye.persist.base;

import java.util.Date;

public class BookList {
    private Long id;

    private String userName;

    private String unreadList;

    private String readList;

    private Integer date;

    private String status;

    private Date gmtCreate;

    private Date gmtModify;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getUnreadList() {
        return unreadList;
    }

    public void setUnreadList(String unreadList) {
        this.unreadList = unreadList == null ? null : unreadList.trim();
    }

    public String getReadList() {
        return readList;
    }

    public void setReadList(String readList) {
        this.readList = readList == null ? null : readList.trim();
    }

    public Integer getDate() {
        return date;
    }

    public void setDate(Integer date) {
        this.date = date;
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

    public Date getGmtModify() {
        return gmtModify;
    }

    public void setGmtModify(Date gmtModify) {
        this.gmtModify = gmtModify;
    }
}