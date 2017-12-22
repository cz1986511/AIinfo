package com.danlu.dleye.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.danlu.dleye.service.SolrService;
import com.danlu.dleye.service.center.HttpService;
import com.danlu.dleye.service.util.DmallDleyeCommon;
import com.danlu.dleye.service.util.DmallSolrCommon;

public class SolrServiceImpl implements SolrService
{
    private final static Logger log = LoggerFactory.getLogger(SolrServiceImpl.class);
    private static String DEFAULTRETURN = "item_id,item_name,item_info,item_product_id,item_category_code,item_category_name,item_brand_code,item_brand_name,item_shop_id,item_price,item_origin_price,item_type";

    @Autowired
    private DmallDleyeCommon dleyeCommon;

    @Override
    public JSONObject searchItemInfos(Map<String, Object> map)
    {
        String baseUrl = DmallSolrCommon.getSolrUrl() + "/" + DmallSolrCommon.getItemCore()
                         + "/select?";
        StringBuilder url = new StringBuilder(baseUrl);
        setItemQueryParams(url, map);
        JSONObject result = HttpService.httpRequest(url.toString(), "GET", null);
        JSONObject response = (JSONObject) result.get("response");
        return response;
    }

    private void setItemQueryParams(StringBuilder url, Map<String, Object> map)
    {
        if (null != map.get("keyWords"))
        {
            String keyWords = escapeQueryChars((String) map.get("keyWords"));
            addQueryField(url, "q", "item_name:" + keyWords + " or item_category_name:" + keyWords
                                    + " or item_brand_name:" + keyWords);
        }
        else if (null != map.get("itemShopId"))
        {
            String keyWords = escapeQueryChars((String) map.get("itemShopId"));
            addQueryField(url, "q", "item_shop_id:" + keyWords);
        }
        if (null != map.get("index"))
        {
            Integer index = (Integer) map.get("index");

            addQueryField(url, "start", String.valueOf((index - 1) * 10));
        }
        else
        {
            addQueryField(url, "start", "0");
        }
        addQueryField(url, "wt", "json");
        // 切面过滤
        setQueryFilter(url, map);
        // 排序
        if (null != map.get("sort"))
        {
            addQueryField(url, "sort", "score desc," + (String) map.get("sort"));
        }
        else
        {
            addQueryField(url, "sort", "score desc,item_price asc");
        }
        // 返回字段
        addQueryField(url, "fl", DEFAULTRETURN);
    }

    @SuppressWarnings("unchecked")
    private void setQueryFilter(StringBuilder url, Map<String, Object> map)
    {
        String amapCode = (String) map.get("amapCode");
        String itemCategoryCode = (String) map.get("itemCategoryCode");
        String itemBrandCode = (String) map.get("itemBrandCode");
        String itemShopId = (String) map.get("itemShopId");
        List<String> shopIds = (List<String>) map.get("shopIds");
        if (!StringUtils.isBlank(amapCode))
        {
            addQueryField(url, "fq", "shop_amap_code:" + amapCode);
        }
        if (!StringUtils.isBlank(itemCategoryCode))
        {
            addQueryField(url, "fq", "item_category_code:" + itemCategoryCode);
        }
        if (!StringUtils.isBlank(itemBrandCode))
        {
            addQueryField(url, "fq", "item_brand_code:" + itemBrandCode);
        }
        if (!StringUtils.isBlank(itemShopId))
        {
            addQueryField(url, "fq", "item_shop_id:" + itemShopId);
        }
        if (null != shopIds && shopIds.size() > 0)
        {
            StringBuilder shopIdStringBuilder = new StringBuilder();
            shopIdStringBuilder.append("(");
            for (String shopId : shopIds)
            {
                shopIdStringBuilder.append(shopId).append(" ");
            }
            shopIdStringBuilder.append(")");
            addQueryField(url, "fq", "item_shop_id:" + shopIdStringBuilder.toString());
        }
    }

    /**
     * 向查询字符串中添加查询字段信息
     * 
     * @param query ： 总的查询字符串对象
     * @param field ： 字段名称
     * @param value ： 字段值
     */
    private void addQueryField(StringBuilder query, String field, String value)
    {
        try
        {
            query.append("&" + field + "=" + URLEncoder.encode(value, "utf-8"));
        }
        catch (UnsupportedEncodingException e)
        {
            log.error("向solr发送http请求参数编码异常，query={},field={},value={}", query, field);
            throw new RuntimeException("构造solr查询请求异常");
        }
    }

    /**
     * 对查询字符串进行转义
     * 
     * @param queryParam ： 查询字符串
     * @return 经过转义的查询字符串
     */
    private String escapeQueryChars(String queryParam)
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < queryParam.length(); i++)
        {
            char c = queryParam.charAt(i);
            // These characters are part of the query syntax and must be escaped
            if (c == '\\' || c == '+' || c == '-' || c == '!' || c == '(' || c == ')' || c == ':'
                || c == '^' || c == '[' || c == ']' || c == '\"' || c == '{' || c == '}'
                || c == '~' || c == '*' || c == '?' || c == '|' || c == '&' || c == ';' || c == '/'
                || Character.isWhitespace(c))
            {
                sb.append('\\');
            }
            sb.append(c);
        }
        return sb.toString();
    }

}
