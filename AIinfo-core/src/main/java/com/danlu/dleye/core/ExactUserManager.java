package com.danlu.dleye.core;

import java.util.List;
import java.util.Map;

import com.danlu.dleye.persist.base.ExactUserInfo;

public interface ExactUserManager
{

    int addExactUserInfo(ExactUserInfo exactUserInfo);

    List<ExactUserInfo> getExactUserInfosByParams(Map<String, Object> map);

    int updateExactUserInfo(ExactUserInfo exactUserInfo);

}
