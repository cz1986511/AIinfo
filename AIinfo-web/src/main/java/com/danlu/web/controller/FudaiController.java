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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.danlu.dleye.client.entity.FudaiDetail;
import com.danlu.dleye.client.entity.JsonResult;
import com.danlu.dleye.constants.FuDaiConstants;
import com.danlu.dleye.service.FudaiService;

@Controller
@RequestMapping("/fudai")
public class FudaiController extends BaseController
{
    private final static Logger log = LoggerFactory.getLogger(FudaiController.class);

    @Autowired
    private FudaiService fudaiService;
    

    /**
     * @Title: getFudaiByfdId
     * @Description: 通过fdId返回福袋信息
     * @param: fdId
     * @return: ResponseEntity<JsonResult<ItemInfo>>
     */
    @RequestMapping(value = "/{fdId}", method = RequestMethod.GET)
    public ResponseEntity<JsonResult<FudaiDetail>> getFudaiByfdId(@PathVariable(value="fdId") String fdId)
    {
        log.info("获取福袋详情入参，fdId={}", fdId);
        JsonResult<FudaiDetail> data = new JsonResult<FudaiDetail>();
        try
        {
            if (StringUtils.isEmpty(fdId))
            {
                data.setMsg("参数为空");
                data.setStatus(0);
                return new ResponseEntity<JsonResult<FudaiDetail>>(data, HttpStatus.OK);
            }
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("isDetail", true);
            List<String> fdIds = new ArrayList<String>();
            fdIds.add(fdId);
            map.put("fdIds", fdIds);
            List<FudaiDetail> list = fudaiService.getFudaiDetails(map);
            if (!CollectionUtils.isEmpty(list))
            {
                data.setData(list.get(0));
            }
            data.setStatus(0);
            data.setMsg("查询成功");
        }
        catch (Exception e)
        {
            log.error("获取福袋详情异常：e={}", e);
            data.setMsg("系统异常");
            data.setStatus(1);
        }
        return new ResponseEntity<JsonResult<FudaiDetail>>(data, HttpStatus.OK);
    }

    /**
     * @Title: getFudaiDetails
     * @Description: 查询福袋列表信息
     * @param:is_my： 是否查我的福袋
     * @param:fd_status： 福袋状态
     * @return:ResponseEntity<JsonResult<List<FudaiDetail>>>
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResponseEntity<JsonResult<List<FudaiDetail>>> getFudaiDetails(HttpServletRequest request,
                                                                         @RequestBody Map<String, Object> condition)
    {
        log.info("查询福袋信息,入参={}", condition);
        JsonResult<List<FudaiDetail>> data = new JsonResult<List<FudaiDetail>>();
        try
        {
                if (this.hasLogin(request))
                {
                    
                    Long userId = (Long) request.getSession().getAttribute("userId");
                    condition.put("userId", userId);
                    if (FuDaiConstants.STATUS.SHARE.equals(condition.get("fdStatus")))
                    {
                        condition.remove("userId");
                    }
                    List<FudaiDetail> list = fudaiService.getFudaiDetails(condition);
                    data.setData(list);
                    data.setStatus(0);
                    data.setMsg("查询成功");
                }
                else
                {
                    log.info("未登录");
                    this.setNotLoginMsg(data);
                    return new ResponseEntity<JsonResult<List<FudaiDetail>>>(data, HttpStatus.OK);
                }
           
        }
        catch (Exception e)
        {
            log.error("查询福袋信息：e={}", e);
            data.setMsg("系统异常");
            data.setStatus(1);
        }
        return new ResponseEntity<JsonResult<List<FudaiDetail>>>(data, HttpStatus.OK);
    }

    /**
     * @Title: addNewFudai
     * @Description: 新增福袋
     * @param:fd_name： 福袋名称
     * @param:items： 福袋商品列表
     * <code>
     *  {
     *      fdName:"双旦福袋",
     *      fudaiItemInfos:[{
     *              fdItemId:"ajfldsjfjsfds",
     *              fdItemNumber:12
     *          }]
     *  }
     * </code>
     * @return:ResponseEntity<JsonResult<String>>
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity<JsonResult<String>> addNewFudai(HttpServletRequest request,
                                                          @RequestBody Map<String, Object> condition)
    {
        log.info("新增福袋信息,入参={}", condition);
        JsonResult<String> data = new JsonResult<String>();
        try
        {
            if (this.hasLogin(request))
            {
                if(condition == null || condition.isEmpty()){
                    throw new NullPointerException("新增福袋参数不能为空.");
                }
                
                String userId = request.getSession().getAttribute("userId").toString();
                String userName = (String) request.getSession().getAttribute("userName");
                condition.put("userId", userId);
                condition.put("userName", userName);
                
                int result = fudaiService.addFudai(condition);
                if (result > 0)
                {
                    data.setStatus(0);
                    data.setMsg("创建成功");
                }
                else
                {
                    data.setStatus(1);
                    data.setMsg("创建失败");
                }
            }
            else
            {
                log.info("未登录");
                this.setNotLoginMsg(data);
                return new ResponseEntity<JsonResult<String>>(data, HttpStatus.OK);
            }
        }
        catch (Exception e)
        {
            log.error("新增福袋信息：e={}", e);
            data.setMsg("系统异常");
            data.setStatus(1);
        }
        return new ResponseEntity<JsonResult<String>>(data, HttpStatus.OK);
    }

    /**
     * @Title: updateFudai
     * @Description: 更新福袋
     * @param:fdId： 福袋id
     * @param:actionType：更新类型(02分享,03订阅,99删除)
     * @param:fdItemId： 商品id
     * @return:ResponseEntity<JsonResult<String>>
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResponseEntity<JsonResult<String>> updateFudai(HttpServletRequest request,
                                                          @RequestBody Map<String, Object> condition)
    {
        log.info("更新福袋信息,入参={}", condition);
        JsonResult<String> data = new JsonResult<String>();
        try
        {
            if (this.hasLogin(request))
            {
                String userId = request.getSession().getAttribute("userId").toString();
                condition.put("userId", Long.valueOf(userId));
                String fdId = (String) condition.get("fdId");
                List<String> fdIds = new ArrayList<String>();
                fdIds.add(fdId);
                condition.put("fdIds", fdIds);
                int result = fudaiService.updateFudai(condition);
                if (result > 0)
                {
                    data.setStatus(0);
                    data.setMsg("操作成功");
                }
                else
                {
                    data.setStatus(1);
                    data.setMsg("操作失败");
                }
            }
            else
            {
                log.info("未登录");
                this.setNotLoginMsg(data);
                return new ResponseEntity<JsonResult<String>>(data, HttpStatus.OK);
            }
        }
        catch (Exception e)
        {
            log.error("更新福袋信息：e={}", e);
            data.setMsg("系统异常");
            data.setStatus(1);
        }
        return new ResponseEntity<JsonResult<String>>(data, HttpStatus.OK);
    }
}
