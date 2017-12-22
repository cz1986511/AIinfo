package com.danlu.dleye.core;

import java.util.List;
import java.util.Map;

import com.danlu.dleye.persist.base.ShopBase;

/**
 * @author chenzhuo@danlu.com
 * @date 2017-09-01 11:22
 */
public interface ShopBaseManager
{

    /**
     * @param: shopName: 店铺名称 shopManager: 店铺负责人 shopTel: 店铺电话 shopType: 店铺类型
     *         shopStatus: 店铺状态(01正常 02锁定 03未激活 99删除) shopAreaCodes:
     *         店铺地图区域code列表(list)
     * @return:List<ShopBase>
     */
    List<ShopBase> getShopBasesByParams(Map<String, Object> map);

    /**
     * @param:shopId 店铺id
     * @return:ShopBase
     */
    ShopBase getShopBaseById(Long shopId);

}
