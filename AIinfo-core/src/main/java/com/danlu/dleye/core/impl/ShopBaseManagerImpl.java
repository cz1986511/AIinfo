package com.danlu.dleye.core.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.danlu.dleye.core.ShopBaseManager;
import com.danlu.dleye.persist.base.ShopBase;
import com.danlu.dleye.persist.mapper.ShopBaseMapper;

public class ShopBaseManagerImpl implements ShopBaseManager
{

    @Autowired
    private ShopBaseMapper shopBaseMapper;

    @Override
    public List<ShopBase> getShopBasesByParams(Map<String, Object> map)
    {
        if (!CollectionUtils.isEmpty(map))
        {
            return shopBaseMapper.selectByParams(map);
        }
        return new ArrayList<ShopBase>();
    }

    @Override
    public ShopBase getShopBaseById(Long shopId)
    {
        if (null != shopId && shopId > 0)
        {
            return shopBaseMapper.selectByPrimaryKey(shopId);
        }
        return null;
    }

}
