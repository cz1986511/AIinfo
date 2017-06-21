package com.danlu.dleye.persist.base;

import java.util.Date;

public class OilInfo {
    private Long id;

    private String province;

    private String oil89;

    private String oil92;

    private String oil95;

    private String oil0;

    private String oil90;

    private String oil93;

    private String oil97;

    private Date updateTime;

    private Date gmtCreate;

    private Date gmtModify;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    public String getOil89() {
        return oil89;
    }

    public void setOil89(String oil89) {
        this.oil89 = oil89 == null ? null : oil89.trim();
    }

    public String getOil92() {
        return oil92;
    }

    public void setOil92(String oil92) {
        this.oil92 = oil92 == null ? null : oil92.trim();
    }

    public String getOil95() {
        return oil95;
    }

    public void setOil95(String oil95) {
        this.oil95 = oil95 == null ? null : oil95.trim();
    }

    public String getOil0() {
        return oil0;
    }

    public void setOil0(String oil0) {
        this.oil0 = oil0 == null ? null : oil0.trim();
    }

    public String getOil90() {
        return oil90;
    }

    public void setOil90(String oil90) {
        this.oil90 = oil90 == null ? null : oil90.trim();
    }

    public String getOil93() {
        return oil93;
    }

    public void setOil93(String oil93) {
        this.oil93 = oil93 == null ? null : oil93.trim();
    }

    public String getOil97() {
        return oil97;
    }

    public void setOil97(String oil97) {
        this.oil97 = oil97 == null ? null : oil97.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
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