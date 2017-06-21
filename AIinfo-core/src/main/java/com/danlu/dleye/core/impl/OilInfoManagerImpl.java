package com.danlu.dleye.core.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.danlu.dleye.core.OilInfoManager;
import com.danlu.dleye.persist.base.OilInfo;
import com.danlu.dleye.persist.mapper.OilInfoMapper;

public class OilInfoManagerImpl implements OilInfoManager {

    @Autowired
    private OilInfoMapper oilInfoMapper;

    @Override
    public int addOilInfo(OilInfo oilInfo) {
        if (null != oilInfo) {
            return oilInfoMapper.insertSelective(oilInfo);
        }
        return 0;
    }

    @Override
    public int updateOilInfo(OilInfo oilInfo) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public OilInfo getOilInfoById(Long id) {
        // TODO Auto-generated method stub
        return null;
    }

}
