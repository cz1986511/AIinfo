package com.danlu.dleye.persist.base;

import java.util.Date;

public class FudaiPicture {
    private Long id;

    private String fdId;

    private Integer fdPicSort;

    private String fdPicUrl;

    private String fdPicStatus;

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

    public Integer getFdPicSort() {
        return fdPicSort;
    }

    public void setFdPicSort(Integer fdPicSort) {
        this.fdPicSort = fdPicSort;
    }

    public String getFdPicUrl() {
        return fdPicUrl;
    }

    public void setFdPicUrl(String fdPicUrl) {
        this.fdPicUrl = fdPicUrl == null ? null : fdPicUrl.trim();
    }

    public String getFdPicStatus() {
        return fdPicStatus;
    }

    public void setFdPicStatus(String fdPicStatus) {
        this.fdPicStatus = fdPicStatus == null ? null : fdPicStatus.trim();
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