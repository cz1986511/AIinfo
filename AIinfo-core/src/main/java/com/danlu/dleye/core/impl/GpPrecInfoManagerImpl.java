package com.danlu.dleye.core.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.danlu.dleye.core.GpPrecInfoManager;
import com.danlu.dleye.persist.base.GpPrecInfo;
import com.danlu.dleye.persist.mapper.GpPrecInfoMapper;

public class GpPrecInfoManagerImpl implements GpPrecInfoManager
{
    @Autowired
    private GpPrecInfoMapper gpPrecInfoMapper;

    @Override
    public int addGpPrecInfo(GpPrecInfo gpPrecInfo)
    {
        if (null != gpPrecInfo)
        {
            return gpPrecInfoMapper.insertSelective(gpPrecInfo);
        }
        return 0;
    }

}
