package com.danlu.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.danlu.dleye.client.entity.JsonResult;
import com.danlu.dleye.client.entity.OrderDetail;
import com.danlu.dleye.service.OrderService;

@Controller
@RequestMapping("/order")
public class OrderController
{
    private final static Logger log = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    /**
     * @Title: getOrderByOrderNo
     * @Description: 通过OrderNo返回订单信息
     * @param: orderNo
     * @return: ResponseEntity<JsonResult<OrderDetail>>
     */
    @RequestMapping(value = "/{orderNo}", method = RequestMethod.GET)
    public ResponseEntity<JsonResult<OrderDetail>> getOrderByOrderNo(HttpServletRequest request,
                                                                     String orderNo)
    {
        log.info("获取订单详情入参，orderNo={}", orderNo);
        JsonResult<OrderDetail> data = new JsonResult<OrderDetail>();
        String userId = "";
        if (null != request.getSession().getAttribute("userId"))
        {
            userId = (String) request.getSession().getAttribute("userId");
            try
            {
                if (StringUtils.isEmpty(orderNo))
                {
                    data.setMsg("参数为空");
                    data.setStatus(0);
                    return new ResponseEntity<JsonResult<OrderDetail>>(data, HttpStatus.OK);
                }
                List<String> orderNos = new ArrayList<String>();
                orderNos.add(orderNo);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("orderNos", orderNos);
                map.put("userId", userId);
                List<OrderDetail> orderDetails = orderService.getOrderList(map);
                if (!CollectionUtils.isEmpty(orderDetails))
                {
                    data.setData(orderDetails.get(0));
                }
                data.setStatus(0);
                data.setMsg("查询成功");
            }
            catch (Exception e)
            {
                log.error("获取订单详情异常：e={}", e);
                data.setMsg("系统异常");
                data.setStatus(1);
            }
        }
        else
        {
            log.info("未登陆");
            data.setStatus(2);
            data.setMsg("请先登陆");
        }

        return new ResponseEntity<JsonResult<OrderDetail>>(data, HttpStatus.OK);
    }

    /**
     * @Title: queryOrders
     * @Description: 多条件查询订单列表信息
     * @param:orderStatus： 订单状态
     * @param:orderType： 订单类型
     * @return:ResponseEntity<JsonResult<List<OrderDetail>>>
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResponseEntity<JsonResult<List<OrderDetail>>> queryOrders(HttpServletRequest request,
                                                                     @RequestBody Map<String, Object> condition)
    {
        log.info("多条件查询订单信息,入参={}", condition);
        JsonResult<List<OrderDetail>> data = new JsonResult<List<OrderDetail>>();
        String userId = "";
        if (null != request.getSession().getAttribute("userId"))
        {
            userId = (String) request.getSession().getAttribute("userId");
            try
            {
                if (!CollectionUtils.isEmpty(condition))
                {
                    condition.put("userId", userId);
                }
                List<OrderDetail> list = orderService.getOrderList(condition);
                data.setData(list);
                data.setStatus(0);
                data.setMsg("查询成功");
            }
            catch (Exception e)
            {
                log.error("多条件查询订单信息：e={}", e);
                data.setMsg("系统异常");
                data.setStatus(1);
            }
        }
        else
        {
            log.info("未登陆");
            data.setStatus(2);
            data.setMsg("请先登陆");
        }
        return new ResponseEntity<JsonResult<List<OrderDetail>>>(data, HttpStatus.OK);
    }
}
