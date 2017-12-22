package com.danlu.dleye.service.util;

public class DmallSolrCommon
{
    /* solr服务器地址 */
    public static String solrUrl;

    /* 商品core */
    public static String itemCore;

    /* 企业core */
    public static String shopCore;

    /* solr查询商品的商品id集合放入商品作为查询条件的阈值core */
    public static Integer solrMaxLimit;

    public static String getSolrUrl()
    {
        return solrUrl;
    }

    public static void setSolrUrl(String solrUrl)
    {
        DmallSolrCommon.solrUrl = solrUrl;
    }

    public static String getItemCore()
    {
        return itemCore;
    }

    public static void setItemCore(String itemCore)
    {
        DmallSolrCommon.itemCore = itemCore;
    }

    public static String getShopCore()
    {
        return shopCore;
    }

    public static void setShopCore(String shopCore)
    {
        DmallSolrCommon.shopCore = shopCore;
    }

    public static Integer getSolrMaxLimit()
    {
        return solrMaxLimit;
    }

    public static void setSolrMaxLimit(Integer solrMaxLimit)
    {
        DmallSolrCommon.solrMaxLimit = solrMaxLimit;
    }

}
