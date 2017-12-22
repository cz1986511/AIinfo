package com.danlu.dleye.persist.mapper;

import java.util.List;
import java.util.Map;

import com.danlu.dleye.persist.base.ItemBase;

public interface ItemBaseMapper
{
    int deleteByPrimaryKey(Long itemId);

    int insert(ItemBase itemBase);

    int insertSelective(ItemBase itemBase);

    ItemBase selectByPrimaryKey(Long itemId);

    int updateByPrimaryKeySelective(ItemBase itemBase);

    int updateByPrimaryKey(ItemBase itemBase);

    List<ItemBase> selectByParams(Map<String, Object> map);
}