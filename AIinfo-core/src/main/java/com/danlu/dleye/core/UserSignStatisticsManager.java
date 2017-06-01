package com.danlu.dleye.core;

import java.util.List;
import java.util.Map;

import com.danlu.dleye.persist.base.UserSignStatistics;

public interface UserSignStatisticsManager {

    int addUserSignStatistics(UserSignStatistics userSignStatistics);

    int deleteUserSignStatistics(Long id);

    UserSignStatistics getUserSignStatisticsById(Long id);

    List<UserSignStatistics> getUserSignStatisticsList(Map<String, Object> map);

    int updateUserSignStatistics(UserSignStatistics userSignStatistics);

}
