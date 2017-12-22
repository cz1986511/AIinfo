package com.danlu.dleye.core.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.danlu.dleye.core.ItemBaseManager;
import com.danlu.dleye.persist.base.ItemBase;
import com.danlu.dleye.persist.mapper.ItemBaseMapper;

public class ItemBaseManagerImpl implements ItemBaseManager
{

    @Autowired
    private ItemBaseMapper itemBaseMapper;

    @Override
    public List<ItemBase> getItemBasesByParams(Map<String, Object> map)
    {
        if (!CollectionUtils.isEmpty(map))
        {
            return itemBaseMapper.selectByParams(map);
        }
        return null;
    }

    @Override
    public ItemBase getItemBaseById(Long itemId)
    {
        if (null != itemId && itemId > 0)
        {
            return itemBaseMapper.selectByPrimaryKey(itemId);
        }
        return null;
    }

}
