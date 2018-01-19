package com.danlu.dleye.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.danlu.dleye.core.GpHisInfoManager;
import com.danlu.dleye.core.GpPrecInfoManager;
import com.danlu.dleye.persist.base.GpHisInfo;
import com.danlu.dleye.persist.base.GpPrecInfo;
import com.danlu.dleye.util.HttpUtil;

public class SharesDateExecutor implements Runnable
{

    private static final Logger logger = LoggerFactory.getLogger(SharesDateExecutor.class);

    private int num;
    private String gpCode;
    private GpHisInfoManager gpHisInfoManager;
    private GpPrecInfoManager gpPrecInfoManager;
    private String url;

    public SharesDateExecutor(GpHisInfoManager gpHisInfoManager,
                              GpPrecInfoManager gpPrecInfoManager, int num, String gpCode,
                              String url)
    {
        super();
        this.num = num;
        this.gpCode = gpCode;
        this.gpHisInfoManager = gpHisInfoManager;
        this.gpPrecInfoManager = gpPrecInfoManager;
        this.url = url;
    }

    @Override
    public void run()
    {
        try
        {
            JSONObject result = HttpUtil.httpRequest(url, "GET", null);
            if (num > 0)
            {
                JSONArray hisJsonArray = result.getJSONObject("data").getJSONObject(gpCode)
                    .getJSONArray("qfqday");
                String gpName = result.getJSONObject("data").getJSONObject(gpCode)
                    .getJSONObject("qt").getJSONArray(gpCode).getString(1);
                for (int i = 0; i < num; i++)
                {
                    JSONArray temp = hisJsonArray.getJSONArray(i);
                    GpHisInfo gpHisInfo = new GpHisInfo();
                    gpHisInfo.setGpCode(gpCode);
                    gpHisInfo.setGpName(gpName);
                    gpHisInfo.setDate(CommonTools.getStringDate(temp.getString(0)));
                    gpHisInfo.setGpOpen(temp.getString(1));
                    gpHisInfo.setGpClose(temp.getString(2));
                    gpHisInfo.setGpHigh(temp.getString(3));
                    gpHisInfo.setGpLow(temp.getString(4));
                    gpHisInfo.setGpVolume(temp.getString(5));
                    gpHisInfoManager.addGpHisInfo(gpHisInfo);
                }
            }
            else
            {
                JSONArray temp = result.getJSONObject("data").getJSONObject(gpCode)
                    .getJSONObject("qt").getJSONArray(gpCode);
                GpPrecInfo gpPrecInfo = new GpPrecInfo();
                gpPrecInfo.setGpName(temp.getString(1));
                gpPrecInfo.setGpCode(gpCode);
                gpPrecInfo.setGpPrecPrice(temp.getString(3));
                gpPrecInfo.setGpClose(temp.getString(4));
                gpPrecInfo.setGpOpen(temp.getString(5));
                gpPrecInfo.setGpVolume(temp.getString(6));
                gpPrecInfo.setGpWp(temp.getString(7));
                gpPrecInfo.setGpNp(temp.getString(8));
                gpPrecInfo.setDatetime(CommonTools.getStringDateToS(temp.getString(30)));
                gpPrecInfo.setGpEx1(temp.getString(31));
                gpPrecInfo.setGpEx2(temp.getString(32));
                gpPrecInfo.setGpHigh(temp.getString(33));
                gpPrecInfo.setGpLow(temp.getString(34));
                gpPrecInfo.setGpAmount(temp.getString(37));
                gpPrecInfo.setGpSyl(temp.getString(39));
                gpPrecInfo.setGpZsz(temp.getString(44));
                gpPrecInfo.setGpLtsz(temp.getString(45));
                gpPrecInfo.setGpQt(result.toJSONString());
                gpPrecInfoManager.addGpPrecInfo(gpPrecInfo);
            }
        }
        catch (Exception e)
        {
            logger.error("SharesDateExecutor is exception:" + e.toString());
        }
    }

}
