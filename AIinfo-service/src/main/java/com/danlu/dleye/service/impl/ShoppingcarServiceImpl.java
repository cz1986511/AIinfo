package com.danlu.dleye.service.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.danlu.dleye.core.ShoppingcarInfoManager;
import com.danlu.dleye.persist.base.ShoppingcarInfo;
import com.danlu.dleye.service.ShoppingcarService;

public class ShoppingcarServiceImpl implements ShoppingcarService
{
    private static final Logger log = LoggerFactory.getLogger(ShoppingcarServiceImpl.class);

    @Autowired
    private ShoppingcarInfoManager shoppingcarInfoManager;

    @Override
    public List<ShoppingcarInfo> getShoppingcarInfos(Map<String, Object> map)
    {
        try
        {
            if (!CollectionUtils.isEmpty(map))
            {
                return shoppingcarInfoManager.getShoppingcarInfosByParams(map);
            }
        }
        catch (Exception e)
        {
            log.error("getShoppingcarInfos is Exception:" + e.toString());
        }
        return null;
    }

    @Override
    public int updateShoppingcarInfo(ShoppingcarInfo shoppingcarInfo)
    {
        try
        {
            if (null != shoppingcarInfo)
            {
                return shoppingcarInfoManager.addOrUpdateShoppingCar(shoppingcarInfo);
            }
        }
        catch (Exception e)
        {
            log.error("updateShoppingcarInfo is Exception:" + e.toString());
        }
        return 0;
    }

    @Override
    public int deleteShoppingcarInfo(ShoppingcarInfo shoppingcarInfo)
    {
        if (null != shoppingcarInfo)
        {
            return shoppingcarInfoManager.deleteShoppingcarInfo(shoppingcarInfo);
        }
        return 0;
    }

}
