package com.danlu.dleye.core;

import java.util.List;
import java.util.Map;

import com.danlu.dleye.persist.base.GpHisInfo;

public interface GpHisInfoManager
{
    int addGpHisInfo(GpHisInfo gpHisInfo);

    List<GpHisInfo> getGpHisInfosByParams(Map<String, Object> map);
}
