package com.danlu.dleye.persist.mapper;

import java.util.List;
import java.util.Map;

import com.danlu.dleye.persist.base.ExactUserInfo;

public interface ExactUserInfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ExactUserInfo exactUserInfo);

    int insertSelective(ExactUserInfo exactUserInfo);

    ExactUserInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ExactUserInfo exactUserInfo);

    int updateByPrimaryKey(ExactUserInfo exactUserInfo);

    List<ExactUserInfo> selectByParams(Map<String, Object> map);
}