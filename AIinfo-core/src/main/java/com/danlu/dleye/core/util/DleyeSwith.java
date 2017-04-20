package com.danlu.dleye.core.util;

import com.danlu.dleye.annotation.Switch;

public class DleyeSwith {

    @Switch(description = "session过期时间,单位分钟", name = "30")
    private Long timeout = 30l;

    @Switch(description = "每页展示条数", name = "pageSize")
    private int pageSize = 10;

    @Switch(description = "每次请求返回条数", name = "requestSize")
    private int requestSize = 100;

    @Switch(description = "redis缓存有效期", name = "effectiveTime")
    private int effectiveTime = 3600;

    @Switch(description = "线程池最大线程数", name = "maxPool")
    private int maxPool = 5;

    @Switch(description = "默认删除30天前数据", name = "defaultDate")
    private int defaultDate = -30;

    @Switch(description = "默认token", name = "defaultDate")
    private String token = "6677";

    @Switch(description = "默认文件路径", name = "filePath")
    private String filePath = "/data/wwwroot/default/rd.xlsx";

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getDefaultDate() {
        return defaultDate;
    }

    public void setDefaultDate(int defaultDate) {
        this.defaultDate = defaultDate;
    }

    public int getMaxPool() {
        return maxPool;
    }

    public void setMaxPool(int maxPool) {
        this.maxPool = maxPool;
    }

    public int getEffectiveTime() {
        return effectiveTime;
    }

    public void setEffectiveTime(int effectiveTime) {
        this.effectiveTime = effectiveTime;
    }

    public int getRequestSize() {
        return requestSize;
    }

    public void setRequestSize(int requestSize) {
        this.requestSize = requestSize;
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
