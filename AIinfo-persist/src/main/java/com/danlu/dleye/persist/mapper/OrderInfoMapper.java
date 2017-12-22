package com.danlu.dleye.persist.mapper;

import com.danlu.dleye.persist.base.OrderInfo;

public interface OrderInfoMapper
{
    int deleteByPrimaryKey(Long id);

    int insert(OrderInfo record);

    int insertSelective(OrderInfo record);

    OrderInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrderInfo record);

    int updateByPrimaryKey(OrderInfo record);
}