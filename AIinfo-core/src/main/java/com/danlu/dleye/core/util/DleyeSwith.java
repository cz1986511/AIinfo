package com.danlu.dleye.core.util;

import com.danlu.dleye.annotation.Switch;

public class DleyeSwith {

    @Switch(description = "session过期时间,单位分钟", name = "30")
    private Long timeout = 30l;

    @Switch(description = "每页展示条数", name = "pageSize")
    private int pageSize = 10;

    @Switch(description = "发件人邮箱", name = "email")
    private String email = "chenzhuo@danlu.com";

    @Switch(description = "发件人密码", name = "password")
    private String password = "ll520@0511";

    @Switch(description = "周年纪念邮件模板", name = "contentTmp")
    private String contentTmp = "<p>亲爱的&#32;&#32;<mark>%s</mark></p><p>&#32;&#32;今天是你加入丹露的%s周年纪念日，感谢这 %s个日日夜夜你对丹露的辛勤付出。健康丹露，快乐丹露！</p>";

    @Switch(description = "面试确认邮件模板", name = "interviewMailTmp")
    private String interviewMailTmp = "<p>亲爱的&#32;&#32;<mark>%s</mark></p><p>&#32;&#32;请点击链接<a href='%s'>确认面试</a>,确认以下面试信息:面试者 :%s,面试岗位:%S,面试时间:%s,面试官:%S";

    @Switch(description = "确认面试地址", name = "insureUrl")
    private String insureUrl = "http://192.168.100.62:9000/dleye-web/insure.html";

    @Switch(description = "activiti_rest接口地址", name = "restUrl")
    private String restUrl = "http://kermit:kermit@192.168.100.67:8088/activiti-rest/service/";

    @Switch(description = "activiti_web接口地址", name = "webUrl")
    private String webUrl = "http://192.168.100.67:8088/activiti-explorer/";

    @Switch(description = "项目流程定义id", name = "projectInstanceId")
    private String projectInstanceId = "projectM:1:12500";

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public String getProjectInstanceId() {
        return projectInstanceId;
    }

    public void setProjectInstanceId(String projectInstanceId) {
        this.projectInstanceId = projectInstanceId;
    }

    public String getRestUrl() {
        return restUrl;
    }

    public void setRestUrl(String restUrl) {
        this.restUrl = restUrl;
    }

    public String getInsureUrl() {
        return insureUrl;
    }

    public void setInsureUrl(String insureUrl) {
        this.insureUrl = insureUrl;
    }

    public String getInterviewMailTmp() {
        return interviewMailTmp;
    }

    public void setInterviewMailTmp(String interviewMailTmp) {
        this.interviewMailTmp = interviewMailTmp;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getContentTmp() {
        return contentTmp;
    }

    public void setContentTmp(String contentTmp) {
        this.contentTmp = contentTmp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public Long getTimeout() {
        return timeout;
    }

    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }

}
