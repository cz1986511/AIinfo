package com.danlu.dleye.core.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.danlu.dleye.client.entity.FudaiDetail;
import com.danlu.dleye.core.FudaiManager;
import com.danlu.dleye.persist.mapper.FudaiInfoMapper;
import com.danlu.dleye.persist.mapper.FudaiItemMapper;

public class FudaiManagerImpl implements FudaiManager
{
    @Autowired
    private FudaiInfoMapper fudaiInfoMapper;
    @Autowired
    private FudaiItemMapper fudaiItemMapper;

    @Override
    public int addFudai(FudaiDetail fudaiDetail)
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int updateFudai(FudaiDetail fudaiDetail)
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public List<FudaiDetail> getFudaiDetailsByParams(Map<String, Object> map)
    {
        // TODO Auto-generated method stub
        return null;
    }

}
