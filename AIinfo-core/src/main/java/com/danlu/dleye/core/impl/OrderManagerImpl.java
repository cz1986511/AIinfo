package com.danlu.dleye.core.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.danlu.dleye.client.entity.OrderDetail;
import com.danlu.dleye.client.entity.OrderItemInfo;
import com.danlu.dleye.core.OrderManager;
import com.danlu.dleye.persist.base.OrderInfo;
import com.danlu.dleye.persist.base.OrderItem;
import com.danlu.dleye.persist.mapper.OrderInfoMapper;
import com.danlu.dleye.persist.mapper.OrderItemMapper;
import com.danlu.dleye.util.DlBeanUtils;

public class OrderManagerImpl implements OrderManager
{
    @Autowired
    private OrderInfoMapper orderInfoMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;

    @Override
    public int addOrder(OrderDetail orderDetail)
    {
        int result = 0;
        if (null != orderDetail)
        {
            OrderInfo orderInfo = new OrderInfo();
            DlBeanUtils.copyProperties(orderDetail, orderInfo);
            result = orderInfoMapper.insertSelective(orderInfo);
            List<OrderItemInfo> orderItemInfos = orderDetail.getOrderItemInfos();
            if (!CollectionUtils.isEmpty(orderItemInfos))
            {
                Iterator<OrderItemInfo> iterator = orderItemInfos.iterator();
                while (iterator.hasNext())
                {
                    OrderItemInfo temp = iterator.next();
                    OrderItem orderItem = new OrderItem();
                    DlBeanUtils.copyProperties(temp, orderItem);
                    orderItemMapper.insertSelective(orderItem);
                }
            }
        }
        return result;
    }

    @Override
    public List<OrderDetail> getOrderDetailsByParams(Map<String, Object> map)
    {
        if (!CollectionUtils.isEmpty(map))
        {
            List<OrderInfo> orderInfos = orderInfoMapper.selectByParams(map);
            if (!CollectionUtils.isEmpty(orderInfos))
            {
                List<OrderDetail> resultList = new ArrayList<OrderDetail>();
                List<String> orderNos = new ArrayList<String>();
                Iterator<OrderInfo> ite1 = orderInfos.iterator();
                while (ite1.hasNext())
                {
                    OrderDetail orderDetail = new OrderDetail();
                    OrderInfo temp = ite1.next();
                    DlBeanUtils.copyProperties(temp, orderDetail);
                    resultList.add(orderDetail);
                    orderNos.add(temp.getOrderNo());
                }
                Map<String, Object> map1 = new HashMap<String, Object>();
                map1.put("orderNos", orderNos);
                List<OrderItem> orderItems = orderItemMapper.selectByParams(map1);
                if (!CollectionUtils.isEmpty(orderItems))
                {
                    Map<String, List<OrderItemInfo>> map2 = new HashMap<String, List<OrderItemInfo>>();
                    Iterator<OrderItem> ite2 = orderItems.iterator();
                    while (ite2.hasNext())
                    {
                        OrderItem temp1 = ite2.next();
                        List<OrderItemInfo> orderItems2 = map2.get(temp1.getOrderNo());
                        OrderItemInfo orderItemInfo = new OrderItemInfo();
                        DlBeanUtils.copyProperties(temp1, orderItemInfo);
                        if (!CollectionUtils.isEmpty(orderItems2))
                        {
                            orderItems2.add(orderItemInfo);
                        }
                        else
                        {
                            orderItems2 = new ArrayList<OrderItemInfo>();
                            orderItems2.add(orderItemInfo);
                            map2.put(temp1.getOrderNo(), orderItems2);
                        }
                    }
                    Iterator<OrderDetail> ite3 = resultList.iterator();
                    while (ite3.hasNext())
                    {
                        OrderDetail temp2 = ite3.next();
                        List<OrderItemInfo> tempItemInfos = map2.get(temp2.getOrderNo());
                        if (!CollectionUtils.isEmpty(tempItemInfos))
                        {
                            temp2.setOrderItemInfos(tempItemInfos);
                        }
                    }
                }
                return resultList;
            }
        }
        return null;
    }

    @Override
    public int updateOrder(OrderDetail orderDetail)
    {
        int result = 0;
        if (null != orderDetail)
        {
            OrderInfo orderInfo = new OrderInfo();
            DlBeanUtils.copyProperties(orderDetail, orderInfo);
            result = orderInfoMapper.updateByPrimaryKeySelective(orderInfo);
            List<OrderItemInfo> orderItemInfos = orderDetail.getOrderItemInfos();
            if (!CollectionUtils.isEmpty(orderItemInfos))
            {
                Iterator<OrderItemInfo> iterator = orderItemInfos.iterator();
                while (iterator.hasNext())
                {
                    OrderItemInfo temp = iterator.next();
                    OrderItem orderItem = new OrderItem();
                    DlBeanUtils.copyProperties(temp, orderItem);
                    orderItemMapper.updateByPrimaryKeySelective(orderItem);
                }
            }
        }
        return result;
    }

}
