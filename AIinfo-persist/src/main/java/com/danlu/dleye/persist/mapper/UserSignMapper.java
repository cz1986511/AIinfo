package com.danlu.dleye.persist.mapper;

import java.util.List;
import java.util.Map;

import com.danlu.dleye.persist.base.UserSign;

public interface UserSignMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserSign userSign);

    int insertSelective(UserSign userSign);

    UserSign selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserSign userSign);

    int updateByPrimaryKey(UserSign userSign);

    List<UserSign> selectUserSignsByParams(Map<String, Object> map);
}