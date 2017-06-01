package com.danlu.dleye.persist.mapper;

import java.util.List;
import java.util.Map;

import com.danlu.dleye.persist.base.UserSignStatistics;

public interface UserSignStatisticsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserSignStatistics record);

    int insertSelective(UserSignStatistics record);

    UserSignStatistics selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserSignStatistics record);

    int updateByPrimaryKey(UserSignStatistics record);

    List<UserSignStatistics> selectUserSignStatisticsByParams(Map<String, Object> map);
}