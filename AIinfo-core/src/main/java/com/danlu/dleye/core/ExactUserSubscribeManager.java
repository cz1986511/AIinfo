package com.danlu.dleye.core;

import java.util.List;
import java.util.Map;

import com.danlu.dleye.persist.base.ExactUserSubscribeInfo;

public interface ExactUserSubscribeManager {

    int addExactUserSubscribe(ExactUserSubscribeInfo exactUserSubscribeInfo);

    List<ExactUserSubscribeInfo> getExactUserSubscribeInfosByParams(Map<String, Object> map);

}
