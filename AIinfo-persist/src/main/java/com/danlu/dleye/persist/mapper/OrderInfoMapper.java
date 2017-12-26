package com.danlu.dleye.persist.mapper;

import java.util.List;
import java.util.Map;

import com.danlu.dleye.persist.base.OrderInfo;

public interface OrderInfoMapper
{
    int deleteByPrimaryKey(Long id);

    int insert(OrderInfo orderInfo);

    int insertSelective(OrderInfo orderInfo);

    OrderInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrderInfo orderInfo);

    List<OrderInfo> selectByParams(Map<String, Object> map);

}