package com.danlu.dleye.persist.mapper;

import com.danlu.dleye.persist.base.OrderOperation;

public interface OrderOperationMapper
{
    int deleteByPrimaryKey(Long id);

    int insert(OrderOperation record);

    int insertSelective(OrderOperation record);

    OrderOperation selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrderOperation record);

    int updateByPrimaryKey(OrderOperation record);
}