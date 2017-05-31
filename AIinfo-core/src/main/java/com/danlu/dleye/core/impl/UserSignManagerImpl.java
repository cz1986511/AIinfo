package com.danlu.dleye.core.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.danlu.dleye.core.UserSignManager;
import com.danlu.dleye.persist.base.UserSign;
import com.danlu.dleye.persist.mapper.UserSignMapper;

public class UserSignManagerImpl implements UserSignManager {

    @Autowired
    private UserSignMapper userSignMapper;

    @Override
    public int addUserSign(UserSign userSign) {
        if (null != userSign) {
            return userSignMapper.insertSelective(userSign);
        } else {
            return 0;
        }
    }

    @Override
    public UserSign getUserSignById(Long id) {
        if (null != id) {
            return userSignMapper.selectByPrimaryKey(id);
        } else {
            return null;
        }
    }

    @Override
    public List<UserSign> getUserSignListByParams(Map<String, Object> map) {
        if (!CollectionUtils.isEmpty(map)) {
            return userSignMapper.selectUserSignsByParams(map);
        } else {
            return null;
        }
    }

    @Override
    public int updateUserSign(UserSign userSign) {
        if (null != userSign) {
            return userSignMapper.updateByPrimaryKeySelective(userSign);
        } else {
            return 0;
        }
    }

    @Override
    public int deletUserSign(Long id) {
        if (null != id) {
            return userSignMapper.deleteByPrimaryKey(id);
        } else {
            return 0;
        }
    }

}
