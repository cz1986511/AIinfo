package com.danlu.dleye.core.impl;

import org.springframework.beans.factory.annotation.Autowired;

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

}
