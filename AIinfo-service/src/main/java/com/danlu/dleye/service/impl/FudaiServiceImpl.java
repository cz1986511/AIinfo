package com.danlu.dleye.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.danlu.dleye.client.entity.FudaiDetail;
import com.danlu.dleye.core.FudaiManager;
import com.danlu.dleye.persist.base.FudaiSubscribe;
import com.danlu.dleye.service.FudaiService;

public class FudaiServiceImpl implements FudaiService
{
    private static final Logger log = LoggerFactory.getLogger(FudaiServiceImpl.class);

    @Autowired
    private FudaiManager fudaiManager;

    @Override
    public int addFudai(Map<String, Object> map)
    {
        int result = 0;
        if (!CollectionUtils.isEmpty(map))
        {
            try
            {
                FudaiDetail fudaiDetail = new FudaiDetail();
                result = fudaiManager.addFudai(fudaiDetail);
            }
            catch (Exception e)
            {
                log.error("addFudai is Exception:" + e.toString());
            }
        }

        return result;
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
                if ("02".equals(actionType))
                {
                    FudaiDetail fudaiDetail = new FudaiDetail();
                    fudaiDetail.setFdId((String) map.get("fdId"));
                    fudaiDetail.setFdStatus("02");
                    result = fudaiManager.updateFudai(fudaiDetail);
                }
                if ("03".equals(actionType))
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
                if ("99".equals(actionType))
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
                return fudaiManager.getFudaiDetailsByParams(map);
            }
            catch (Exception e)
            {
                log.error("getFudaiDetails is Exception:" + e.toString());
            }
        }
        return null;
    }

}
