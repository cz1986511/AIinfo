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
import com.danlu.dleye.persist.base.AddressInfo;
import com.danlu.dleye.service.AddressService;

@Controller
@RequestMapping("/address")
public class AddressController
{
    private final static Logger log = LoggerFactory.getLogger(AddressController.class);

    @Autowired
    private AddressService addressService;

    /**
     * @Title: queryAddressList
     * @Description: 查询用户收货地址
     * @return:ResponseEntity<JsonResult<List<AddressInfo>>>
     */
    @RequestMapping(value = "/list", method = { RequestMethod.POST, RequestMethod.GET })
    public ResponseEntity<JsonResult<List<AddressInfo>>> queryAddressList(HttpServletRequest request)
    {
        JsonResult<List<AddressInfo>> data = new JsonResult<List<AddressInfo>>();
        String userId = "";
        if (null != request.getSession().getAttribute("userId"))
        {
            userId = "" + request.getSession().getAttribute("userId");
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("userId", userId);
            try
            {
                List<AddressInfo> result = addressService.getAddressInfos(map);
                data.setData(result);
                data.setStatus(0);
                data.setMsg("查询成功");
            }
            catch (Exception e)
            {
                log.error("查询用户收货地址：e={}", e);
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
        return new ResponseEntity<JsonResult<List<AddressInfo>>>(data, HttpStatus.OK);
    }

    /**
     * @Title: updateAddress
     * @Description: 更新用户收货地址
     * @return:ResponseEntity<JsonResult<String>>
     */
    @RequestMapping(value = "/update", method = { RequestMethod.POST, RequestMethod.GET })
    public ResponseEntity<JsonResult<String>> updateAddress(HttpServletRequest request,
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
                AddressInfo addressInfo = new AddressInfo();
                addressInfo.setUserId(userId);
                if (null != condition.get("action") && null != condition.get("addressId"))
                {
                    String addressId = (String) condition.get("addressId");
                    addressInfo.setAddressId(addressId);
                    result = addressService.deleteAddress(addressInfo);
                }
                else
                {
                    String addressName = (String) condition.get("addressName");
                    String addressTel = (String) condition.get("addressTel");
                    String addressDetail = (String) condition.get("addressDetail");
                    String addressZipcode = (String) condition.get("addressZipcode");
                    addressInfo.setAddressName(addressName);
                    addressInfo.setAddressTel(addressTel);
                    addressInfo.setAddressInfo(addressDetail);
                    addressInfo.setAddressZipcode(addressZipcode);
                    addressInfo.setAddressStatus("01");
                    if (null != condition.get("addressId"))
                    {
                        String addressId = (String) condition.get("addressId");
                        addressInfo.setAddressId(addressId);
                    }
                    result = addressService.addOrUpdateAddress(addressInfo);
                }
                if (result > 0)
                {
                    data.setStatus(0);
                    data.setMsg("更新收货地址成功");
                }
                else
                {
                    data.setStatus(1);
                    data.setMsg("更新收货地址失败");
                }
            }
            catch (Exception e)
            {
                log.error("更新收货地址异常：e={}", e);
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
