package com.danlu.dleye.persist.base;

import java.util.Date;

public class GpHisInfo {
    private Long id;

    private String gpCode;

    private String gpName;

    private Date date;

    private String gpOpen;

    private String gpClose;

    private String gpHigh;

    private String gpLow;

    private String gpVolume;

    private String gpAmount;

    private Date gmtCreate;

    private Date gmtModified;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGpCode() {
        return gpCode;
    }

    public void setGpCode(String gpCode) {
        this.gpCode = gpCode == null ? null : gpCode.trim();
    }

    public String getGpName() {
        return gpName;
    }

    public void setGpName(String gpName) {
        this.gpName = gpName == null ? null : gpName.trim();
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getGpOpen() {
        return gpOpen;
    }

    public void setGpOpen(String gpOpen) {
        this.gpOpen = gpOpen == null ? null : gpOpen.trim();
    }

    public String getGpClose() {
        return gpClose;
    }

    public void setGpClose(String gpClose) {
        this.gpClose = gpClose == null ? null : gpClose.trim();
    }

    public String getGpHigh() {
        return gpHigh;
    }

    public void setGpHigh(String gpHigh) {
        this.gpHigh = gpHigh == null ? null : gpHigh.trim();
    }

    public String getGpLow() {
        return gpLow;
    }

    public void setGpLow(String gpLow) {
        this.gpLow = gpLow == null ? null : gpLow.trim();
    }

    public String getGpVolume() {
        return gpVolume;
    }

    public void setGpVolume(String gpVolume) {
        this.gpVolume = gpVolume == null ? null : gpVolume.trim();
    }

    public String getGpAmount() {
        return gpAmount;
    }

    public void setGpAmount(String gpAmount) {
        this.gpAmount = gpAmount == null ? null : gpAmount.trim();
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