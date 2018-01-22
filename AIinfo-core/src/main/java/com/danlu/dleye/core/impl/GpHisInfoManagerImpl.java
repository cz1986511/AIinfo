package com.danlu.dleye.core.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.danlu.dleye.core.GpHisInfoManager;
import com.danlu.dleye.persist.base.GpHisInfo;
import com.danlu.dleye.persist.mapper.GpHisInfoMapper;

public class GpHisInfoManagerImpl implements GpHisInfoManager
{
    @Autowired
    private GpHisInfoMapper gpHisInfoMapper;

    @Override
    public int addGpHisInfo(GpHisInfo gpHisInfo)
    {
        if (null != gpHisInfo)
        {
            return gpHisInfoMapper.insertSelective(gpHisInfo);
        }
        return 0;
    }

    @Override
    public List<GpHisInfo> getGpHisInfosByParams(Map<String, Object> map)
    {
        if (!CollectionUtils.isEmpty(map))
        {
            return gpHisInfoMapper.selectByParams(map);
        }
        return null;
    }

}
