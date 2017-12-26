package com.danlu.dleye.service.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.danlu.dleye.client.entity.OrderDetail;
import com.danlu.dleye.core.OrderManager;
import com.danlu.dleye.service.OrderService;

public class OrderServiceImpl implements OrderService
{
    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);
    @Autowired
    private OrderManager orderManger;

    @Override
    public int addOrder(OrderDetail orderDetail)
    {
        if (null != orderDetail)
        {
            try
            {
                orderManger.addOrder(orderDetail);
            }
            catch (Exception e)
            {
                log.error("addOrder is Exception:" + e.toString());
            }
        }
        return 0;
    }

    @Override
    public int updateOrder(OrderDetail orderDetail)
    {
        if (null != orderDetail)
        {
            try
            {
                orderManger.updateOrder(orderDetail);
            }
            catch (Exception e)
            {
                log.error("updateOrder is Exception:" + e.toString());
            }
        }
        return 0;
    }

    @Override
    public List<OrderDetail> getOrderList(Map<String, Object> map)
    {
        if (!CollectionUtils.isEmpty(map))
        {
            try
            {
                return orderManger.getOrderDetailsByParams(map);
            }
            catch (Exception e)
            {
                log.error("getOrderList is Exception:" + e.toString());
            }
        }
        return null;
    }

}
