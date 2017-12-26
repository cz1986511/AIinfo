package com.danlu.dleye.service;

import java.util.List;
import java.util.Map;

import com.danlu.dleye.persist.base.ShoppingcarInfo;

public interface ShoppingcarService
{
    List<ShoppingcarInfo> getShoppingcarInfos(Map<String, Object> map);

    int updateShoppingcarInfo(ShoppingcarInfo shoppingcarInfo);

    int deleteShoppingcarInfo(ShoppingcarInfo shoppingcarInfo);

}
