package com.danlu.dleye.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.danlu.dleye.core.GpHisInfoManager;
import com.danlu.dleye.persist.base.GpHisInfo;
import com.danlu.dleye.service.SharesService;

public class SharesServiceImpl implements SharesService
{
    @Autowired
    private GpHisInfoManager gpHisInfoManager;

    @Override
    public List<GpHisInfo> getGpHisInfos(Map<String, Object> map)
    {
        if (!CollectionUtils.isEmpty(map))
        {
            return gpHisInfoManager.getGpHisInfosByParams(map);
        }
        return null;
    }

}
