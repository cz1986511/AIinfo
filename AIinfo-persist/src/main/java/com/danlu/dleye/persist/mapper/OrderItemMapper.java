package com.danlu.dleye.persist.mapper;

import java.util.List;
import java.util.Map;

import com.danlu.dleye.persist.base.OrderItem;

public interface OrderItemMapper
{
    int deleteByPrimaryKey(Long id);

    int insert(OrderItem orderItem);

    int insertSelective(OrderItem orderItem);

    OrderItem selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrderItem orderItem);

    List<OrderItem> selectByParams(Map<String, Object> map);
}