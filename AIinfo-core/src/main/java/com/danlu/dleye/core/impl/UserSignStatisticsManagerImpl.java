package com.danlu.dleye.core.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.danlu.dleye.core.UserSignStatisticsManager;
import com.danlu.dleye.persist.base.UserSignStatistics;
import com.danlu.dleye.persist.mapper.UserSignStatisticsMapper;

public class UserSignStatisticsManagerImpl implements UserSignStatisticsManager {

    @Autowired
    private UserSignStatisticsMapper userSignStatisticsMapper;

    @Override
    public int addUserSignStatistics(UserSignStatistics userSignStatistics) {
        if (null != userSignStatistics) {
            return userSignStatisticsMapper.insertSelective(userSignStatistics);
        } else {
            return 0;
        }
    }

    @Override
    public int deleteUserSignStatistics(Long id) {
        if (null != id) {
            return userSignStatisticsMapper.deleteByPrimaryKey(id);
        } else {
            return 0;
        }
    }

    @Override
    public UserSignStatistics getUserSignStatisticsById(Long id) {
        if (null != id) {
            return userSignStatisticsMapper.selectByPrimaryKey(id);
        } else {
            return null;
        }
    }

    @Override
    public List<UserSignStatistics> getUserSignStatisticsList(Map<String, Object> map) {
        if (!CollectionUtils.isEmpty(map)) {
            return userSignStatisticsMapper.selectUserSignStatisticsByParams(map);
        } else {
            return null;
        }
    }

    @Override
    public int updateUserSignStatistics(UserSignStatistics userSignStatistics) {
        if (null != userSignStatistics) {
            return userSignStatisticsMapper.updateByPrimaryKeySelective(userSignStatistics);
        } else {
            return 0;
        }
    }

}
