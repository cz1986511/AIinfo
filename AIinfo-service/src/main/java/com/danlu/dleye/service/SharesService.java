package com.danlu.dleye.service;

import java.util.List;
import java.util.Map;

import com.danlu.dleye.persist.base.GpHisInfo;

public interface SharesService
{
    List<GpHisInfo> getGpHisInfos(Map<String, Object> map);

}
