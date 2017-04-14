package com.danlu.dleye.persist.mapper;

import java.util.List;
import java.util.Map;

import com.danlu.dleye.persist.base.UserInfoEntity;

public interface UserInfoEntityMapper {
    int deleteByPrimaryKey(Long userId);

    Long insert(UserInfoEntity record);

    Long insertSelective(UserInfoEntity record);

    UserInfoEntity selectByPrimaryKey(Long userId);

    int updateByPrimaryKeySelective(UserInfoEntity record);

    int updateByPrimaryKey(UserInfoEntity record);

    List<UserInfoEntity> selectUserInfoEntitiesByParams(Map<String, Object> map);

    Long selectUserInfoListCount(Map<String, Object> map);
}