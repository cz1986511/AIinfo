package com.danlu.dleye.core;

import java.util.List;
import java.util.Map;

import com.danlu.dleye.client.entity.OrderDetail;

public interface OrderManager
{
    int addOrder(OrderDetail orderDetail);

    List<OrderDetail> getOrderDetailsByParams(Map<String, Object> map);

    int updateOrder(OrderDetail orderDetail);

}
