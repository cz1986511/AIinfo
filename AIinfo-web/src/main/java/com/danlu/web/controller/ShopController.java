package com.danlu.web.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.danlu.dleye.client.entity.JsonResult;
import com.danlu.dleye.client.entity.ShopInfo;
import com.danlu.dleye.service.ShopService;

@Controller
@RequestMapping("/shop")
public class ShopController
{

    @Autowired
    private ShopService shopService;

    private final static Logger log = LoggerFactory.getLogger(ShopController.class);

    /**
     * @Title: getShopByShopId
     * @Description: 通过shopId返回店铺信息
     * @param: shopId
     * @return: ResponseEntity<JsonResult<ShopBase>>
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<JsonResult<ShopInfo>> getShopByShopId(@PathVariable("id") final String shopId)
    {
        log.info("获取店铺详情入参，shopId={}", shopId);
        JsonResult<ShopInfo> data = new JsonResult<ShopInfo>();
        try
        {
            if (StringUtils.isEmpty(shopId))
            {
                data.setMsg("参数为空");
                data.setStatus(0);
                return new ResponseEntity<JsonResult<ShopInfo>>(data, HttpStatus.OK);
            }
            ShopInfo shopInfo = shopService.getShopBaseById(Long.valueOf(shopId));
            data.setData(shopInfo);
            data.setStatus(0);
            data.setMsg("查询成功");
        }
        catch (Exception e)
        {
            log.error("获取店铺详情异常：e={}", e);
            data.setMsg("系统异常");
            data.setStatus(1);
        }
        return new ResponseEntity<JsonResult<ShopInfo>>(data, HttpStatus.OK);
    }

    /**
     * @Title: queryShops
     * @Description: 多条件查询店铺列表信息
     * @param:userLongitude： 地图坐标经度
     * @param:userLatitude： 地图坐标纬度
     * @param:amapCode： 高德地图区域code
     * @return:ResponseEntity<JsonResult<List<ShopBase>>>
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResponseEntity<JsonResult<List<ShopInfo>>> queryShops(@RequestBody Map<String, Object> condition)
    {
        log.info("多条件查询店铺信息,入参={}", condition);
        JsonResult<List<ShopInfo>> datas = new JsonResult<List<ShopInfo>>();
        try
        {
            List<ShopInfo> shops = shopService.getShopsAroundUserLocation(condition);
            datas.setData(shops);
            datas.setStatus(0);
            datas.setMsg("查询成功");
        }
        catch (Exception e)
        {
            log.error("多条件查询店铺信息：e={}", e);
            datas.setMsg("系统异常");
            datas.setStatus(1);
        }
        return new ResponseEntity<JsonResult<List<ShopInfo>>>(datas, HttpStatus.OK);
    }

    public static void main(String[] args)
    {
        String test = "3214231";
        System.out.println(Long.valueOf(test));
    }
}
