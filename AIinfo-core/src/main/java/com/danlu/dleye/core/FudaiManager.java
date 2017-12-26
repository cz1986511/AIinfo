package com.danlu.dleye.core;

import java.util.List;
import java.util.Map;

import com.danlu.dleye.client.entity.FudaiDetail;

public interface FudaiManager
{
    int addFudai(FudaiDetail fudaiDetail);

    int updateFudai(FudaiDetail fudaiDetail);

    List<FudaiDetail> getFudaiDetailsByParams(Map<String, Object> map);

}
