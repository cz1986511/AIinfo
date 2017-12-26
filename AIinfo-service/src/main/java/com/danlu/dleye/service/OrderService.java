package com.danlu.dleye.service;

import java.util.List;
import java.util.Map;

import com.danlu.dleye.client.entity.OrderDetail;

public interface OrderService
{
    int addOrder(OrderDetail orderDetail);

    int updateOrder(OrderDetail orderDetail);

    List<OrderDetail> getOrderList(Map<String, Object> map);

}
