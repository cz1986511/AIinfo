package com.danlu.web.base;

import java.io.Serializable;

public class StatisticsBase implements Serializable {

    private static final long serialVersionUID = -4738190457389120L;

    private String date; //统计时间

    private Long departmentId; //部门id

    private String department; //部门名称

    private int employeeNum; //人数

    private Long postId; //岗位id

    private String post; //岗位名称

    private int noticeNum; //通知面试人数

    private int interviewNum; //参加面试人数

    private int unInterviewNum; //未参加面试人数

    private int passInterviewNum;//通过面试人数

    private int entryNum; //入职人数

    private int interviewRate; //面试率

    private int passInterviewRate;//面试通过率

    private int entryRate; //入职率

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getEmployeeNum() {
        return employeeNum;
    }

    public void setEmployeeNum(int employeeNum) {
        this.employeeNum = employeeNum;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public int getNoticeNum() {
        return noticeNum;
    }

    public void setNoticeNum(int noticeNum) {
        this.noticeNum = noticeNum;
    }

    public int getInterviewNum() {
        return interviewNum;
    }

    public void setInterviewNum(int interviewNum) {
        this.interviewNum = interviewNum;
    }

    public int getUnInterviewNum() {
        return unInterviewNum;
    }

    public void setUnInterviewNum(int unInterviewNum) {
        this.unInterviewNum = unInterviewNum;
    }

    public int getPassInterviewNum() {
        return passInterviewNum;
    }

    public void setPassInterviewNum(int passInterviewNum) {
        this.passInterviewNum = passInterviewNum;
    }

    public int getEntryNum() {
        return entryNum;
    }

    public void setEntryNum(int entryNum) {
        this.entryNum = entryNum;
    }

    public int getInterviewRate() {
        return interviewRate;
    }

    public void setInterviewRate(int interviewRate) {
        this.interviewRate = interviewRate;
    }

    public int getPassInterviewRate() {
        return passInterviewRate;
    }

    public void setPassInterviewRate(int passInterviewRate) {
        this.passInterviewRate = passInterviewRate;
    }

    public int getEntryRate() {
        return entryRate;
    }

    public void setEntryRate(int entryRate) {
        this.entryRate = entryRate;
    }

}
