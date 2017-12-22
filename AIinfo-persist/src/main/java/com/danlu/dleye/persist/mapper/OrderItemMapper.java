package com.danlu.dleye.persist.mapper;

import com.danlu.dleye.persist.base.OrderItem;

public interface OrderItemMapper
{
    int deleteByPrimaryKey(Long id);

    int insert(OrderItem record);

    int insertSelective(OrderItem record);

    OrderItem selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrderItem record);

    int updateByPrimaryKey(OrderItem record);
}