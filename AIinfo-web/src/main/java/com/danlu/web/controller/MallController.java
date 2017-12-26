package com.danlu.web.controller;

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

import com.alibaba.fastjson.JSONObject;
import com.danlu.dleye.client.entity.ItemInfo;
import com.danlu.dleye.client.entity.JsonResult;
import com.danlu.dleye.service.ItemService;

@Controller
@RequestMapping("/mall")
public class MallController
{

    @Autowired
    private ItemService itemService;

    private final static Logger log = LoggerFactory.getLogger(MallController.class);

    /**
     * @Title: getItemInfoByItemId
     * @Description: 通过itemId返回商品信息
     * @param: shopId
     * @return: ResponseEntity<JsonResult<ItemInfo>>
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<JsonResult<ItemInfo>> getItemInfoByItemId(@PathVariable("id") final String itemId)
    {
        log.info("获取商品详情入参，itemId={}", itemId);
        JsonResult<ItemInfo> data = new JsonResult<ItemInfo>();
        try
        {
            if (StringUtils.isEmpty(itemId))
            {
                data.setMsg("参数为空");
                data.setStatus(0);
                return new ResponseEntity<JsonResult<ItemInfo>>(data, HttpStatus.OK);
            }
            ItemInfo itemInfo = itemService.getItemById(Long.valueOf(itemId));
            data.setData(itemInfo);
            data.setStatus(0);
            data.setMsg("查询成功");
        }
        catch (Exception e)
        {
            log.error("获取商品详情异常：e={}", e);
            data.setMsg("系统异常");
            data.setStatus(1);
        }
        return new ResponseEntity<JsonResult<ItemInfo>>(data, HttpStatus.OK);
    }

    /**
     * @Title: queryShops
     * @Description: 多条件查询商品列表信息
     * @param:userLongitude： 地图坐标经度
     * @param:userLatitude： 地图坐标纬度
     * @param:amapCode： 高德地图区域code
     * @param:shopId: 店铺id
     * @return:ResponseEntity<JsonResult<JSONArray>>
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResponseEntity<JsonResult<JSONObject>> queryItems(@RequestBody Map<String, Object> condition)
    {
        log.info("多条件查询商品信息,入参={}", condition);
        JsonResult<JSONObject> datas = new JsonResult<JSONObject>();
        try
        {
            JSONObject items = itemService.getItemsByParamsFromSolr(condition);
            datas.setData(items);
            datas.setStatus(0);
            datas.setMsg("查询成功");
        }
        catch (Exception e)
        {
            log.error("多条件查询商品信息：e={}", e);
            datas.setMsg("系统异常");
            datas.setStatus(1);
        }
        return new ResponseEntity<JsonResult<JSONObject>>(datas, HttpStatus.OK);
    }
}
