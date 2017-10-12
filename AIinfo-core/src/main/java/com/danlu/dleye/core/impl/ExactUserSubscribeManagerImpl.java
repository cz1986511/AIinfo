package com.danlu.dleye.core.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.danlu.dleye.core.ExactUserSubscribeManager;
import com.danlu.dleye.persist.base.ExactUserSubscribeInfo;
import com.danlu.dleye.persist.mapper.ExactUserSubscribeInfoMapper;

public class ExactUserSubscribeManagerImpl implements ExactUserSubscribeManager {

    @Autowired
    private ExactUserSubscribeInfoMapper exactUserSubscribeInfoMapper;

    @Override
    public int addExactUserSubscribe(ExactUserSubscribeInfo exactUserSubscribeInfo) {
        if (null != exactUserSubscribeInfo) {
            return exactUserSubscribeInfoMapper.insertSelective(exactUserSubscribeInfo);
        }
        return 0;
    }

    @Override
    public List<ExactUserSubscribeInfo> getExactUserSubscribeInfosByParams(Map<String, Object> map) {
        if (!CollectionUtils.isEmpty(map)) {
            return exactUserSubscribeInfoMapper.selectByParams(map);
        }
        return null;
    }

}
