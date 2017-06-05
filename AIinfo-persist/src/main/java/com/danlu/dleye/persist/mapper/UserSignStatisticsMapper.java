package com.danlu.dleye.persist.mapper;

import java.util.List;
import java.util.Map;

import com.danlu.dleye.persist.base.UserSignStatistics;

public interface UserSignStatisticsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserSignStatistics userSignStatistics);

    int insertSelective(UserSignStatistics userSignStatistics);

    UserSignStatistics selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserSignStatistics userSignStatistics);

    int updateByPrimaryKey(UserSignStatistics userSignStatistics);

    List<UserSignStatistics> selectUserSignStatisticsByParams(Map<String, Object> map);
}