package com.danlu.dleye.core.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.danlu.dleye.core.ShoppingcarInfoManager;
import com.danlu.dleye.persist.base.ShoppingcarInfo;
import com.danlu.dleye.persist.mapper.ShoppingcarInfoMapper;

public class ShoppingcarInfoManagerImpl implements ShoppingcarInfoManager
{
    @Autowired
    ShoppingcarInfoMapper shoppingcarInfoMapper;

    @Override
    public int addOrUpdateShoppingCar(ShoppingcarInfo shoppingcarInfo)
    {
        if (null != shoppingcarInfo)
        {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("userId", shoppingcarInfo.getUserId());
            map.put("itemId", shoppingcarInfo.getItemId());
            map.put("itemType", shoppingcarInfo.getItemType());
            List<ShoppingcarInfo> list = shoppingcarInfoMapper.selectByParams(map);
            if (!CollectionUtils.isEmpty(list))
            {
                return shoppingcarInfoMapper.updateByPrimaryKeySelective(shoppingcarInfo);
            }
            else
            {
                return shoppingcarInfoMapper.insertSelective(shoppingcarInfo);
            }
        }
        return 0;
    }

    @Override
    public List<ShoppingcarInfo> getShoppingcarInfosByParams(Map<String, Object> map)
    {
        if (!CollectionUtils.isEmpty(map))
        {
            return shoppingcarInfoMapper.selectByParams(map);
        }
        return null;
    }

    @Override
    public int deleteShoppingcarInfo(ShoppingcarInfo shoppingcarInfo)
    {
        if (null != shoppingcarInfo)
        {
            return shoppingcarInfoMapper.deleteShoppingcarInfo(shoppingcarInfo);
        }
        return 0;
    }

}
