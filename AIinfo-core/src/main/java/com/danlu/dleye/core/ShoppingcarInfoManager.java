package com.danlu.dleye.core;

import java.util.List;
import java.util.Map;

import com.danlu.dleye.persist.base.ShoppingcarInfo;

public interface ShoppingcarInfoManager
{
    int addOrUpdateShoppingCar(ShoppingcarInfo shoppingcarInfo);

    List<ShoppingcarInfo> getShoppingcarInfosByParams(Map<String, Object> map);

    int deleteShoppingcarInfo(ShoppingcarInfo shoppingcarInfo);

}
