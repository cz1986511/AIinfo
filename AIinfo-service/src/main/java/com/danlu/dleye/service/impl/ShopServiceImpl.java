package com.danlu.dleye.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.TypeReference;
import com.danlu.dleye.client.entity.ShopInfo;
import com.danlu.dleye.core.ShopBaseManager;
import com.danlu.dleye.core.util.RedisClient;
import com.danlu.dleye.persist.base.ShopBase;
import com.danlu.dleye.service.ShopService;
import com.danlu.dleye.service.util.DmallDleyeCommon;
import com.danlu.dleye.service.util.DoubleCompartor;
import com.danlu.dleye.util.DlBeanUtils;

public class ShopServiceImpl implements ShopService
{

    private final static Logger log = LoggerFactory.getLogger(ShopServiceImpl.class);

    @Autowired
    private ShopBaseManager shopBaseManager;
    @Autowired
    private RedisClient redisClient;
    @Autowired
    private DmallDleyeCommon dleyeCommon;

    @Override
    public ShopInfo getShopBaseById(Long shopId)
    {
        ShopInfo shopInfo = null;
        try
        {
            if (null != shopId && shopId > 0)
            {
                shopInfo = redisClient.get("shopBase:" + shopId, new TypeReference<ShopInfo>()
                {
                });
                if (null == shopInfo)
                {
                    ShopBase shopBase = shopBaseManager.getShopBaseById(shopId);
                    if (null != shopBase)
                    {
                        DlBeanUtils.copyProperties(shopBase, shopInfo);
                        redisClient.set("shopBase:" + shopId, shopInfo, 3600);
                    }
                    else
                    {
                        log.info("getShopInfoById shopId is null|shopId:" + shopId);
                    }
                }
            }
        }
        catch (Exception e)
        {
            log.error("getShopBaseById:" + shopId + " is Exception:" + e.toString());
        }
        return shopInfo;
    }

    @Override
    public List<ShopInfo> getShopsAroundUserLocation(Map<String, Object> inputData)
    {
        int defaultShowNum = 0;
        String userArea = (String) inputData.get("amapCode");
        String userLongitudeString = (String) inputData.get("userLongitude");
        String userLatitudeString = (String) inputData.get("userLatitude");
        Integer showNums = (Integer) inputData.get("count");
        if (null != showNums && showNums > 0)
        {
            defaultShowNum = showNums;
        }
        else
        {
            defaultShowNum = dleyeCommon.getShopShowNums();
        }
        if (StringUtils.isBlank(userLongitudeString) || StringUtils.isBlank(userLatitudeString))
        {
            userLongitudeString = dleyeCommon.getDefaultLongitude();
            userLatitudeString = dleyeCommon.getDefaultLatitude();
            userArea = dleyeCommon.getDefaultArea();
        }
        Double userLongitude = Double.valueOf(userLongitudeString);
        Double userLatitude = Double.valueOf(userLatitudeString);
        List<ShopBase> list = redisClient.get(userArea + "shopBases",
            new TypeReference<List<ShopBase>>()
            {
            });
        if (!CollectionUtils.isEmpty(list))
        {
            return changeBaseToInfo(filterShopList(userLongitude, userLatitude,
                dleyeCommon.getDefaultR(), list, defaultShowNum));
        }
        else
        {
            Map<String, Object> map = new HashMap<String, Object>();
            List<String> shopAreas = new ArrayList<String>();
            List<String> userAreas = new ArrayList<String>();
            userAreas.add(userArea);
            // List<AreaDTO> cityAreaDTOs =
            // areaHttpService.getAreaListByAmapCodeList(userAreas);
            // if (!CollectionUtils.isEmpty(cityAreaDTOs))
            // {
            // List<AreaDTO> areaDTOs =
            // areaHttpService.getAreaListByParentCode(cityAreaDTOs
            // .get(0).getAreaCode());
            // if (!CollectionUtils.isEmpty(areaDTOs))
            // {
            // Iterator<AreaDTO> iterator = areaDTOs.iterator();
            // while (iterator.hasNext())
            // {
            // shopAreas.add(iterator.next().getAreaCode());
            // }
            // map.put("shopAreaCodes", shopAreas);
            // map.put("shopStatus", "01");
            // list = shopBaseManager.getShopBasesByParams(map);
            // if (!CollectionUtils.isEmpty(list))
            // {
            // redisClient.set(userArea + "shopBases", list, 3600);
            // return changeBaseToInfo(filterShopList(userLongitude,
            // userLatitude,
            // dleyeCommon.getDefaultR(), list, defaultShowNum));
            // }
            // }
            // }
        }
        return new ArrayList<ShopInfo>();
    }

    // 通过经纬度过滤店铺
    private List<ShopBase> filterShopList(Double userLongitude, Double userLatitude, Double r,
                                          List<ShopBase> list, int showNums)
    {
        if (list.size() < showNums)
        {
            return list;
        }
        List<ShopBase> result = new ArrayList<ShopBase>();
        Map<Double, List<ShopBase>> map = new HashMap<Double, List<ShopBase>>();
        List<Double> listKey = new ArrayList<Double>();
        if (!CollectionUtils.isEmpty(list))
        {
            Iterator<ShopBase> ite = list.iterator();
            while (ite.hasNext())
            {
                ShopBase temp = ite.next();
                Double longitude = Double.valueOf(temp.getShopLongitude());
                Double latitude = Double.valueOf(temp.getShopLatitude());
                Double key = (longitude - userLongitude) * (longitude - userLongitude)
                             + (latitude - userLatitude) * (latitude - userLatitude) - r * r;
                listKey.add(key);
                List<ShopBase> tempList = map.get(key);
                if (CollectionUtils.isEmpty(tempList))
                {
                    tempList = new ArrayList<ShopBase>();
                }
                tempList.add(temp);
                map.put(key, tempList);
            }
            List<Double> newList = new ArrayList<Double>(new HashSet<Double>(listKey));
            DoubleCompartor compartor = new DoubleCompartor();
            Collections.sort(newList, compartor);
            for (int i = 0; i < showNums; i++)
            {
                result.addAll((map.get(newList.get(i))));
            }
        }
        return result;
    }

    private List<ShopInfo> changeBaseToInfo(List<ShopBase> list)
    {
        List<ShopInfo> result = new ArrayList<ShopInfo>();
        if (!CollectionUtils.isEmpty(list))
        {
            Iterator<ShopBase> iterator = list.iterator();
            while (iterator.hasNext())
            {
                ShopInfo temp = new ShopInfo();
                DlBeanUtils.copyProperties(iterator.next(), temp);
                result.add(temp);
            }
        }
        return result;
    }

}
