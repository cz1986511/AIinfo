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

    @Switch(description = "默认删除180天前数据", name = "defaultDate")
    private int defaultDate = -180;

    @Switch(description = "默认token", name = "defaultDate")
    private String token = "6677";

    @Switch(description = "默认文件路径", name = "filePath")
    private String filePath = "/data/file/rd.xlsx";

    @Switch(description = "天气预报城市,支持多个城市以逗号隔开", name = "citys")
    private String citys = "chengdu";

    @Switch(description = "中午菜单,以逗号隔开", name = "lunchs")
    private String lunchs = "73°炜面,麦当劳,廖记棒棒鸡,全家,沈小福,三顾冒菜,阿san的饭,德克士,subway,关刀冒菜,必胜客,好吃得一哔bi,最小一家,甘食记,宜宾燃面,徐歪嘴,小乔砂锅,煎饼郎,三国炙烤肉,猪八戒烤肉饭,大盘鸡,有米海鲜粥,谢记家常菜,跷脚牛肉,花甲米线,香天下火锅,土碗香,成都一号,聚缘川菜馆,争鲜回转寿司";

    @Switch(description = "读书分享会会员名单", name = "member")
    private String member = "陈卓,陈家川,周巧扬,胡道庭,唐正攀,张梦娇,夏耩,何苗,王梅,贾旭飞,缪攀,周龙波,李一方,郑娟,罗浪,凌小华,刘兵,胡凯,张虎";

    @Switch(description = "当前书单期数", name = "bookListDate")
    private int bookListDate = 1;

    public int getBookListDate() {
        return bookListDate;
    }

    public void setBookListDate(int bookListDate) {
        this.bookListDate = bookListDate;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public String getLunchs() {
        return lunchs;
    }

    public void setLunchs(String lunchs) {
        this.lunchs = lunchs;
    }

    public String getCitys() {
        return citys;
    }

    public void setCitys(String citys) {
        this.citys = citys;
    }

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
