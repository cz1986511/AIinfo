package com.danlu.dleye.service;

import java.util.List;
import java.util.Map;

import com.danlu.dleye.client.entity.ShopInfo;

/**
 * @author: chenzhuo@danlu.com
 * @date: 2017-09-01 13:47
 */
public interface ShopService
{

    /**
     * @param: shopId 店铺id
     * @return: shopInfo
     */
    ShopInfo getShopBaseById(Long shopId);

    /**
     * @param: userLongitude:用户地图坐标经度 userLatitude: 用户地图坐标维度 areaCode:
     *         用户所在坐标地图区域
     * @return: List<ShopBase>
     */
    List<ShopInfo> getShopsAroundUserLocation(Map<String, Object> inputData);

}
