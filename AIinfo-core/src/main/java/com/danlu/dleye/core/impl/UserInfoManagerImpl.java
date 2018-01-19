package com.danlu.dleye.core.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.danlu.dleye.core.UserInfoManager;
import com.danlu.dleye.persist.base.UserInfoEntity;
import com.danlu.dleye.persist.mapper.UserInfoEntityMapper;

public class UserInfoManagerImpl implements UserInfoManager {

    @Autowired
    private UserInfoEntityMapper userInfoEntityMapper;

    @Override
    public UserInfoEntity getUserInfoByUserId(Long userId) {
        if (null != userId) {
            return userInfoEntityMapper.selectByPrimaryKey(userId);
        }
        return null;
    }

    @Override
    public List<UserInfoEntity> getUserListByParams(Map<String, Object> map) {
        if (!CollectionUtils.isEmpty(map)) {
            return userInfoEntityMapper.selectUserInfoEntitiesByParams(map);
        }
        return null;
    }

    @Override
    public Long addUserInfo(UserInfoEntity userInfoEntity) {
        if (null != userInfoEntity) {
            userInfoEntity.setDelFlag(0);
            return userInfoEntityMapper.insert(userInfoEntity);
        }
        return 0l;
    }

    @Override
    public int updateUserInfo(UserInfoEntity userInfoEntity) {
        if (null != userInfoEntity) {
            return userInfoEntityMapper.updateByPrimaryKeySelective(userInfoEntity);
        }
        return 0;
    }

    @Override
    public Long getUserListCount(Map<String, Object> map) {
        if (!CollectionUtils.isEmpty(map)) {
            return userInfoEntityMapper.selectUserInfoListCount(map);
        }
        return 0l;
    }

}
