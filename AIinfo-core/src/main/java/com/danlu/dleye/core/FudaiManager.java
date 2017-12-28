package com.danlu.dleye.core;

import java.util.List;
import java.util.Map;

import com.danlu.dleye.client.entity.FudaiDetail;
import com.danlu.dleye.persist.base.FudaiSubscribe;

public interface FudaiManager
{
    int addFudai(FudaiDetail fudaiDetail);

    int addFudaiSubscribe(FudaiSubscribe fudaiSubscribe);

    int updateFudai(FudaiDetail fudaiDetail);

    int deleteFudai(Map<String, Object> map);

    List<FudaiDetail> getFudaiDetailsByParams(Map<String, Object> map);

    List<FudaiSubscribe> getFudaiSubscribes(Map<String, Object> map);

}
