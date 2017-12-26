package com.danlu.web.controller;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.danlu.dleye.client.entity.JsonResult;
import com.danlu.dleye.core.util.CommonTools;
import com.danlu.dleye.persist.base.ShoppingcarInfo;
import com.danlu.dleye.service.ItemService;
import com.danlu.dleye.service.ShoppingcarService;

@Controller
@RequestMapping("/shoppingcar")
public class ShoppingcarController
{
    private final static Logger log = LoggerFactory.getLogger(ShoppingcarController.class);

    @Autowired
    private ItemService itemService;
    @Autowired
    private ShoppingcarService shoppingcarService;

    /**
     * @Title: queryShoppingcarItems
     * @Description: 查询购物车商品列表信息
     * @return:ResponseEntity<JsonResult<List<ShoppingcarInfo>>>
     */
    @RequestMapping(value = "/list", method = { RequestMethod.POST, RequestMethod.GET })
    public ResponseEntity<JsonResult<List<ShoppingcarInfo>>> queryShoppingcarItems(HttpServletRequest request)
    {
        JsonResult<List<ShoppingcarInfo>> datas = new JsonResult<List<ShoppingcarInfo>>();
        String userId = "";
        if (null != request.getSession().getAttribute("userId"))
        {
            userId = "" + request.getSession().getAttribute("userId");
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("userId", userId);
            try
            {
                List<ShoppingcarInfo> result = shoppingcarService.getShoppingcarInfos(map);
                datas.setData(result);
                datas.setStatus(0);
                datas.setMsg("查询成功");
            }
            catch (Exception e)
            {
                log.error("多条件查询商品信息：e={}", e);
                datas.setMsg("系统异常");
                datas.setStatus(1);
            }
        }
        else
        {
            log.info("未登陆");
            datas.setStatus(2);
            datas.setMsg("请先登陆");
        }
        return new ResponseEntity<JsonResult<List<ShoppingcarInfo>>>(datas, HttpStatus.OK);
    }

    /**
     * @Title: updateShoppingcarItems
     * @Description: 更新购物车商品列表信息
     * @return:ResponseEntity<JsonResult<String>>
     */
    @RequestMapping(value = "/update", method = { RequestMethod.POST, RequestMethod.GET })
    public ResponseEntity<JsonResult<String>> updateShoppingcarItems(HttpServletRequest request,
                                                                     @RequestBody Map<String, Object> condition)
    {
        JsonResult<String> data = new JsonResult<String>();
        String userId = "";
        if (null != request.getSession().getAttribute("userId"))
        {
            try
            {
                int result = 0;
                userId = "" + request.getSession().getAttribute("userId");
                String itemId = (String) condition.get("itemId");
                String itemType = (String) condition.get("itemType");
                ShoppingcarInfo shoppingcarInfo = new ShoppingcarInfo();
                shoppingcarInfo.setItemId(Long.valueOf(itemId));
                shoppingcarInfo.setItemType(itemType);
                shoppingcarInfo.setUserId(userId);
                if (null != condition.get("action"))
                {
                    result = shoppingcarService.deleteShoppingcarInfo(shoppingcarInfo);
                }
                else
                {
                    String itemName = (String) condition.get("itemName");
                    String itemPrice = (String) condition.get("itemPrice");
                    String itemNum = (String) condition.get("itemNum");
                    shoppingcarInfo.setShoppingcarId(CommonTools.generateDbKey());
                    shoppingcarInfo.setItemName(itemName);
                    shoppingcarInfo.setItemNumber(Integer.valueOf(itemNum));
                    shoppingcarInfo.setItemPrice(Long.valueOf(itemPrice));
                    result = shoppingcarService.updateShoppingcarInfo(shoppingcarInfo);
                }
                if (result > 0)
                {
                    data.setData("itemId:" + itemId);
                    data.setStatus(0);
                    data.setMsg("更新购物车成功");
                }
                else
                {
                    data.setData("itemId:" + itemId);
                    data.setStatus(1);
                    data.setMsg("更新购物车失败");
                }
            }
            catch (Exception e)
            {
                log.error("更新购物车异常：e={}", e);
                data.setStatus(1);
                data.setMsg("系统异常");
            }
        }
        else
        {
            log.info("未登陆");
            data.setStatus(2);
            data.setMsg("请先登陆");
        }
        return new ResponseEntity<JsonResult<String>>(data, HttpStatus.OK);
    }
}
