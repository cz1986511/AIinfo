package com.danlu.dleye.core.util;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.danlu.dleye.core.GpHisInfoManager;
import com.danlu.dleye.core.GpPrecInfoManager;
import com.danlu.dleye.util.HttpUtil;

public class SharesUtil
{

    private static Logger logger = LoggerFactory.getLogger(SharesUtil.class);

    @Autowired
    private GpPrecInfoManager gpPrecInfoManager;
    @Autowired
    private GpHisInfoManager gpHisInfoManager;
    @Autowired
    private DleyeSwith dleyeSwith;

    private static String DEFAULT_URL = "http://web.ifzq.gtimg.cn/appstock/app/fqkline/get?_var=kline_dayqfq&param=PARAM,day,,,NUM,qfq&r=RANDOM";
    private ThreadPoolExecutor executor;

    public void init()
    {
        this.executor = new ThreadPoolExecutor(2, dleyeSwith.getMaxPool(), 2000,
            TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
    }

    public void addGpHisInfo()
    {
        try
        {
            if (null == executor)
            {
                init();
            }
            String[] sharasCodes = dleyeSwith.getSharesCodes().split(";");
            int num = dleyeSwith.getNum();
            for (int i = 0; i < sharasCodes.length; i++)
            {
                String sharasCode = sharasCodes[i];
                String url = DEFAULT_URL.replace("PARAM", sharasCode).replace("NUM", "" + num);
                SharesDateExecutor sharesDateExecutor = new SharesDateExecutor(gpHisInfoManager,
                    gpPrecInfoManager, num, sharasCode, url);
                executor.execute(sharesDateExecutor);
            }

        }
        catch (Exception e)
        {
            logger.error("addGpHisInfo is exception:" + e.toString());
        }

    }

    public void addGpPrecInfo()
    {
        try
        {
            if (null == executor)
            {
                init();
            }
            String[] sharasCodes = dleyeSwith.getSharesCodes().split(";");
            int num = 0;
            for (int i = 0; i < sharasCodes.length; i++)
            {
                String sharasCode = sharasCodes[i];
                String url = DEFAULT_URL.replace("PARAM", sharasCode).replace("NUM", "" + num)
                    .replace("RANDOM", "" + Math.random());
                SharesDateExecutor sharesDateExecutor = new SharesDateExecutor(gpHisInfoManager,
                    gpPrecInfoManager, num, sharasCode, url);
                executor.execute(sharesDateExecutor);
            }
        }
        catch (Exception e)
        {
            logger.error("addGpPrecInfo is exception:" + e.toString());
        }
    }

    public static void main(String[] args)
    {
        String pgCode = "sh600519";
        String url = DEFAULT_URL.replace("PARAM", pgCode).replace("NUM", "" + 0)
            .replace("RANDOM", "" + Math.random());
        JSONObject result = HttpUtil.httpRequest(url, "GET", null);
        System.out.println(result.getJSONObject("data").getJSONObject(pgCode).getJSONObject("qt")
            .getJSONArray(pgCode));
        System.out.println(Math.random());
        System.out.println(CommonTools.getStringDateToS("20180119151328"));
    }

}
