package com.danlu.dleye.core.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.danlu.dleye.core.ExactUserManager;
import com.danlu.dleye.persist.base.ExactUserInfo;
import com.danlu.dleye.persist.mapper.ExactUserInfoMapper;

public class ExactUserManagerImpl implements ExactUserManager {

    @Autowired
    private ExactUserInfoMapper exactUserinfoMapper;

    @Override
    public int addExactUserInfo(ExactUserInfo exactUserInfo) {
        if (null != exactUserInfo) {
            return exactUserinfoMapper.insertSelective(exactUserInfo);
        }
        return 0;
    }

    @Override
    public List<ExactUserInfo> getExactUserInfosByParams(Map<String, Object> map) {
        if (!CollectionUtils.isEmpty(map)) {
            return exactUserinfoMapper.selectByParams(map);
        }
        return null;
    }

}
