package com.danlu.dleye.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.danlu.dleye.client.entity.FudaiDetail;
import com.danlu.dleye.client.entity.FudaiItemInfo;
import com.danlu.dleye.client.entity.ItemInfo;
import com.danlu.dleye.constants.FuDaiConstants;
import com.danlu.dleye.core.FudaiManager;
import com.danlu.dleye.persist.base.FudaiSubscribe;
import com.danlu.dleye.service.FudaiService;
import com.danlu.dleye.service.ItemService;
import com.danlu.dleye.util.DlBeanUtils;

public class FudaiServiceImpl implements FudaiService
{
    private static final Logger log = LoggerFactory.getLogger(FudaiServiceImpl.class);

    @Autowired
    private FudaiManager fudaiManager;
    
    @Autowired
    private ItemService itemService;

    @Override
    public int addFudai(Map<String, Object> map)
    {
        int result = 0;
        if (!CollectionUtils.isEmpty(map))
        {
            try
            {
                FudaiDetail fudaiDetail = new FudaiDetail();
                DlBeanUtils.copyDTOProperties(map, fudaiDetail);
                if(StringUtils.isBlank(fudaiDetail.getFdName())){
                    throw new NullPointerException("福袋名称不能为空.");
                }
                if(CollectionUtils.isEmpty(fudaiDetail.getFudaiItemInfos())){
                    throw new NullPointerException("福袋item不能为空.");
                }
                fudaiDetail.setCreatePerson((String)map.get("userName"));
                this.setItemDetailInfo(fudaiDetail);
                result = fudaiManager.addFudai(fudaiDetail);
            }
            catch (Exception e)
            {
                log.error("addFudai is Exception:" + e.toString());
            }
        }
        return result;
    }
    
    private void setItemDetailInfo(FudaiDetail fudaiDetail){
        Map<Long,FudaiItemInfo> itemMap  = new HashMap<Long, FudaiItemInfo>();
        for (FudaiItemInfo item : fudaiDetail.getFudaiItemInfos())
        {
            if(item != null && item.getFdItemId() != null){
                itemMap.put(item.getFdItemId(), item);
            }
        }
        if(!CollectionUtils.isEmpty(itemMap)){
            Map<String, Object> inputData = new HashMap<String, Object>();
            inputData.put("itemIds", itemMap.keySet());
            log.info("查询商品基础信息入参params=>{}.",inputData);
            List<ItemInfo> items = this.itemService.getItemsByParams(inputData);
            if(!CollectionUtils.isEmpty(items)){
                FudaiItemInfo _item = null;
                Long originPrice = 0L;
                Long price = 0L;
                for (ItemInfo item : items)
                {
                    if(item != null){
                        _item = itemMap.get(item.getItemId());
                        if(_item != null){
                            _item.setFdItemName(item.getItemName());
                            _item.setFdItemOriginPrice(item.getItemOriginPrice());
                            _item.setFdItemPrice(item.getItemPrice());
                            _item.setFdItemPic(item.getItemDesc());
                            originPrice = originPrice +((item.getItemOriginPrice() == null ?0:item.getItemOriginPrice()) * _item.getFdItemNumber());
                            price = price +( (item.getItemPrice() == null ?0:item.getItemPrice()) * _item.getFdItemNumber());
                        }
                    }
                }
                fudaiDetail.setFdAmount(originPrice);
                fudaiDetail.setFdPrice(price);
                fudaiDetail.setFudaiItemInfos(new ArrayList<FudaiItemInfo>(itemMap.values()));
                fudaiDetail.setFdStatus(FuDaiConstants.STATUS.CREATE);
            }
        }
    }

    @Override
    public int updateFudai(Map<String, Object> map)
    {
        int result = 0;
        if (!CollectionUtils.isEmpty(map))
        {
            try
            {
                String actionType = (String) map.get("actionType");
                if (FuDaiConstants.STATUS.SHARE.equals(actionType))
                {
                    FudaiDetail fudaiDetail = new FudaiDetail();
                    fudaiDetail.setFdId((String) map.get("fdId"));
                    fudaiDetail.setFdStatus("02");
                    result = fudaiManager.updateFudai(fudaiDetail);
                }
                if (FuDaiConstants.STATUS.SUBSCRIBE.equals(actionType))
                {
                    List<Long> userIds = new ArrayList<Long>();
                    Long userId = (Long) map.get("userId");
                    userIds.add(userId);
                    map.put("userIds", userIds);
                    List<FudaiSubscribe> subList = fudaiManager.getFudaiSubscribes(map);
                    if (CollectionUtils.isEmpty(subList))
                    {
                        FudaiSubscribe fudaiSubscribe = new FudaiSubscribe();
                        fudaiSubscribe.setFdId((String) map.get("fdId"));
                        fudaiSubscribe.setUserId((Long) map.get("userId"));
                        result = fudaiManager.addFudaiSubscribe(fudaiSubscribe);
                    }
                    else
                    {
                        result = 1;
                    }
                }
                if (FuDaiConstants.STATUS.DELETE.equals(actionType))
                {
                    result = fudaiManager.deleteFudai(map);
                }
            }
            catch (Exception e)
            {
                log.error("updateFudai is Exception:" + e.toString());
            }
        }
        return result;
    }

    @Override
    public List<FudaiDetail> getFudaiDetails(Map<String, Object> map)
    {
        if (!CollectionUtils.isEmpty(map))
        {
            try
            {
                map.put("notEqStatus", FuDaiConstants.STATUS.DELETE);
                Set<String> fudaiIds = new HashSet<String>();
                Long userId = (Long) map.get("userId");
                if (null != map.get("isMy"))
                {
                    log.info("查询我的订阅福袋入参=>{}",map);
                    List<FudaiSubscribe>  subs = this.getSubscribes(userId);
                    if(!CollectionUtils.isEmpty(subs)){
                       for (FudaiSubscribe fudaiSubscribe : subs)
                        {
                            if(fudaiSubscribe != null && StringUtils.isNotBlank(fudaiSubscribe.getFdId())){
                                fudaiIds.add(fudaiSubscribe.getFdId());
                            }
                        }
                    }
                    Map<String,Object> map1 = new HashMap<String, Object>(map);
                    map1.remove("isDetail");
                    log.info("查询我的创建福袋入参=>{}",map1);
                    List<FudaiDetail> list =  this.fudaiManager.getFudaiDetailsByParams(map1);
                    if(!CollectionUtils.isEmpty(list)){
                        for (FudaiDetail fudaiDetail : list)
                        {
                            if(fudaiDetail != null && StringUtils.isNotBlank(fudaiDetail.getFdId())){
                                fudaiIds.add(fudaiDetail.getFdId());
                            }
                        }
                    }
                    log.info("查询我的福袋共=>{}",(fudaiIds == null ?0:fudaiIds.size()));
                    if(!CollectionUtils.isEmpty(fudaiIds)){
                        map.remove("userId");
                        map.put("fdIds", fudaiIds);
                    }else{
                        return new ArrayList<FudaiDetail>();
                    }
                }
                log.info("查询福袋列表入参=>{}",map);
                return fudaiManager.getFudaiDetailsByParams(map);
            }
            catch (Exception e)
            {
                log.error("getFudaiDetails is Exception:" ,e);
            }
        }
        return null;
    }
    
    private List<FudaiSubscribe> getSubscribes(Long userId){
        Map<String,Object> map = new HashMap<String, Object>();
        if(userId != null){
            List<Long> userIds = new ArrayList<Long>();
            userIds.add(userId);
            map.put("userIds", userIds);
        }
        try
        {
            return this.fudaiManager.getFudaiSubscribes(map);
        }
        catch (Exception e)
        {
            log.info("查询{}已订阅福袋异常.",userId);
            throw e;
        }
    }

}
