package com.danlu.dleye.persist.mapper;

import java.util.List;
import java.util.Map;

import com.danlu.dleye.persist.base.ExactUserSubscribeInfo;

public interface ExactUserSubscribeInfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ExactUserSubscribeInfo exactUserSubscribeInfo);

    int insertSelective(ExactUserSubscribeInfo exactUserSubscribeInfo);

    ExactUserSubscribeInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ExactUserSubscribeInfo exactUserSubscribeInfo);

    int updateByPrimaryKey(ExactUserSubscribeInfo exactUserSubscribeInfo);

    List<ExactUserSubscribeInfo> selectByParams(Map<String, Object> map);
}