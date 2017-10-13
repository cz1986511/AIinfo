package com.danlu.dleye.persist.base;

import java.util.Date;

public class ExactUserInfo {
    private Long id;

    private String userOpenId;

    private String userName;

    private String userCompanyName;

    private String userCompanyId;

    private String userCompanyType;

    private String userTel;

    private String userWxSubscribe;

    private String userWxNickname;

    private String userWxSex;

    private String userWxCity;

    private String userWxProvince;

    private String userWxCountry;

    private String userWxLanguage;

    private String userWxHeadimgurl;

    private String userWxSubscribeTime;

    private String userWxRemark;

    private String userWxGroupid;

    private String userWxTagidList;

    private String userStatus;

    private Date gmtCreate;

    private Date gmtModified;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserOpenId() {
        return userOpenId;
    }

    public void setUserOpenId(String userOpenId) {
        this.userOpenId = userOpenId == null ? null : userOpenId.trim();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getUserCompanyName() {
        return userCompanyName;
    }

    public void setUserCompanyName(String userCompanyName) {
        this.userCompanyName = userCompanyName == null ? null : userCompanyName.trim();
    }

    public String getUserCompanyId() {
        return userCompanyId;
    }

    public void setUserCompanyId(String userCompanyId) {
        this.userCompanyId = userCompanyId == null ? null : userCompanyId.trim();
    }

    public String getUserCompanyType() {
        return userCompanyType;
    }

    public void setUserCompanyType(String userCompanyType) {
        this.userCompanyType = userCompanyType == null ? null : userCompanyType.trim();
    }

    public String getUserTel() {
        return userTel;
    }

    public void setUserTel(String userTel) {
        this.userTel = userTel == null ? null : userTel.trim();
    }

    public String getUserWxSubscribe() {
        return userWxSubscribe;
    }

    public void setUserWxSubscribe(String userWxSubscribe) {
        this.userWxSubscribe = userWxSubscribe == null ? null : userWxSubscribe.trim();
    }

    public String getUserWxNickname() {
        return userWxNickname;
    }

    public void setUserWxNickname(String userWxNickname) {
        this.userWxNickname = userWxNickname == null ? null : userWxNickname.trim();
    }

    public String getUserWxSex() {
        return userWxSex;
    }

    public void setUserWxSex(String userWxSex) {
        this.userWxSex = userWxSex == null ? null : userWxSex.trim();
    }

    public String getUserWxCity() {
        return userWxCity;
    }

    public void setUserWxCity(String userWxCity) {
        this.userWxCity = userWxCity == null ? null : userWxCity.trim();
    }

    public String getUserWxProvince() {
        return userWxProvince;
    }

    public void setUserWxProvince(String userWxProvince) {
        this.userWxProvince = userWxProvince == null ? null : userWxProvince.trim();
    }

    public String getUserWxCountry() {
        return userWxCountry;
    }

    public void setUserWxCountry(String userWxCountry) {
        this.userWxCountry = userWxCountry == null ? null : userWxCountry.trim();
    }

    public String getUserWxLanguage() {
        return userWxLanguage;
    }

    public void setUserWxLanguage(String userWxLanguage) {
        this.userWxLanguage = userWxLanguage == null ? null : userWxLanguage.trim();
    }

    public String getUserWxHeadimgurl() {
        return userWxHeadimgurl;
    }

    public void setUserWxHeadimgurl(String userWxHeadimgurl) {
        this.userWxHeadimgurl = userWxHeadimgurl == null ? null : userWxHeadimgurl.trim();
    }

    public String getUserWxSubscribeTime() {
        return userWxSubscribeTime;
    }

    public void setUserWxSubscribeTime(String userWxSubscribeTime) {
        this.userWxSubscribeTime = userWxSubscribeTime == null ? null : userWxSubscribeTime.trim();
    }

    public String getUserWxRemark() {
        return userWxRemark;
    }

    public void setUserWxRemark(String userWxRemark) {
        this.userWxRemark = userWxRemark == null ? null : userWxRemark.trim();
    }

    public String getUserWxGroupid() {
        return userWxGroupid;
    }

    public void setUserWxGroupid(String userWxGroupid) {
        this.userWxGroupid = userWxGroupid == null ? null : userWxGroupid.trim();
    }

    public String getUserWxTagidList() {
        return userWxTagidList;
    }

    public void setUserWxTagidList(String userWxTagidList) {
        this.userWxTagidList = userWxTagidList == null ? null : userWxTagidList.trim();
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus == null ? null : userStatus.trim();
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