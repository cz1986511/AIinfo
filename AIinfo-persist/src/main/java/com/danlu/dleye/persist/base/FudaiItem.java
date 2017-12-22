package com.danlu.dleye.persist.base;

import java.util.Date;

public class FudaiItem {
    private Long id;

    private String fdId;

    private Long fdItemId;

    private String fdItemName;

    private Date gmtCreate;

    private Date gmtModified;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFdId() {
        return fdId;
    }

    public void setFdId(String fdId) {
        this.fdId = fdId == null ? null : fdId.trim();
    }

    public Long getFdItemId() {
        return fdItemId;
    }

    public void setFdItemId(Long fdItemId) {
        this.fdItemId = fdItemId;
    }

    public String getFdItemName() {
        return fdItemName;
    }

    public void setFdItemName(String fdItemName) {
        this.fdItemName = fdItemName == null ? null : fdItemName.trim();
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