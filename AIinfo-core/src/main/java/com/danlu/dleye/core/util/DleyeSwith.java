package com.danlu.dleye.core.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.TypeReference;
import com.danlu.dleye.annotation.Switch;

public class DleyeSwith
{

    @Autowired
    private RedisClient redisClient;

    @Switch(description = "session过期时间,单位分钟", name = "timeout")
    private Long timeout = 60l;

    @Switch(description = "每页展示条数", name = "pageSize")
    private int pageSize = 10;

    @Switch(description = "每次请求返回条数", name = "requestSize")
    private int requestSize = 100;

    @Switch(description = "redis缓存有效期", name = "effectiveTime")
    private int effectiveTime = 3600;

    @Switch(description = "线程池最大线程数", name = "maxPool")
    private int maxPool = 5;

    @Switch(description = "默认删除180天前数据", name = "defaultDate")
    private int defaultDate = -18000;

    @Switch(description = "借书周期", name = "borrowDate")
    private int borrowDate = 30;

    @Switch(description = "默认token", name = "token")
    private String token = "6677";

    @Switch(description = "默认文件路径", name = "filePath")
    private String filePath = "/data/file/rd.xlsx";

    @Switch(description = "天气预报城市,支持多个城市以逗号隔开", name = "citys")
    private String citys = "chengdu";

    @Switch(description = "读书分享会会员名单", name = "member")
    private String member = "周巧扬,胡道庭,唐正攀,张梦娇,夏耩,何苗,王梅,贾旭飞,缪攀,周龙波,李一方,郑娟,罗浪,凌小华,刘兵,胡凯,张虎,陈卓,陈家川,刘阳,罗灵";

    @Switch(description = "当前书单期数", name = "bookListDate")
    private int bookListDate = 1;

    @Switch(description = "是否生成当前书单", name = "isMakeBookList")
    private boolean isMakeBookList = false;

    @Switch(description = "随机数长度", name = "wisdomNum")
    private int wisdomNum = 201;

    @Switch(description = "默认取前一天的数据", name = "num")
    private int num = 1;

    @Switch(description = "股票代码列表", name = "sharesCodes")
    private String sharesCodes = "sh600519;sh601318;sz000858;sz000568;sz000001";

    public String getSharesCodes()
    {
        return sharesCodes;
    }

    public void setSharesCodes(String sharesCodes)
    {
        this.sharesCodes = sharesCodes;
    }

    public int getNum()
    {
        return num;
    }

    public void setNum(int num)
    {
        this.num = num;
    }

    public int getBorrowDate()
    {
        return borrowDate;
    }

    public void setBorrowDate(int borrowDate)
    {
        this.borrowDate = borrowDate;
    }

    public int getWisdomNum()
    {
        return wisdomNum;
    }

    public void setWisdomNum(int wisdomNum)
    {
        this.wisdomNum = wisdomNum;
    }

    public boolean getMakeBookList()
    {
        return this.isMakeBookList;
    }

    public void setMakeBookList(boolean isMakeBookList)
    {
        this.isMakeBookList = isMakeBookList;
    }

    public int getBookListDate()
    {
        return bookListDate;
    }

    public void setBookListDate(int bookListDate)
    {
        this.bookListDate = bookListDate;
    }

    public String getMember()
    {
        return member;
    }

    public void setMember(String member)
    {
        this.member = member;
    }

    public String getCitys()
    {
        return citys;
    }

    public void setCitys(String citys)
    {
        this.citys = citys;
    }

    public String getFilePath()
    {
        return filePath;
    }

    public void setFilePath(String filePath)
    {
        this.filePath = filePath;
    }

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    public int getDefaultDate()
    {
        return defaultDate;
    }

    public void setDefaultDate(int defaultDate)
    {
        this.defaultDate = defaultDate;
    }

    public int getMaxPool()
    {
        return maxPool;
    }

    public void setMaxPool(int maxPool)
    {
        this.maxPool = maxPool;
    }

    public int getEffectiveTime()
    {
        return effectiveTime;
    }

    public void setEffectiveTime(int effectiveTime)
    {
        this.effectiveTime = effectiveTime;
    }

    public int getRequestSize()
    {
        return requestSize;
    }

    public void setRequestSize(int requestSize)
    {
        this.requestSize = requestSize;
    }

    public int getPageSize()
    {
        return pageSize;
    }

    public void setPageSize(int pageSize)
    {
        this.pageSize = pageSize;
    }

    public Long getTimeout()
    {
        return timeout;
    }

    public void setTimeout(Long timeout)
    {
        this.timeout = timeout;
    }

    public String getWisdom()
    {
        try
        {
            int k = (int) (Math.random() * getWisdomNum());
            String key = "wisdom-" + k;
            return redisClient.get(key, new TypeReference<String>()
            {
            });
        }
        catch (Exception e)
        {
            e.toString();
        }
        return "书中自有黄金屋，书中自有颜如玉。";
    }

    public static void main(String[] args)
    {
        try
        {
            File file = new File("/data/file/wisdom.txt");// Text文件
            BufferedReader br = new BufferedReader(new FileReader(file));// 构造一个BufferedReader类来读取文件
            String s = null;
            while ((s = br.readLine()) != null)
            {// 使用readLine方法，一次读一行
                System.out.println(s);
            }
            br.close();
        }
        catch (Exception e)
        {
            e.toString();
        }
    }

}
