package com.danlu.dleye.persist.mapper;

import java.util.List;
import java.util.Map;

import com.danlu.dleye.persist.base.GpHisInfo;

public interface GpHisInfoMapper
{
    int deleteByPrimaryKey(Long id);

    int insert(GpHisInfo gpHisInfo);

    int insertSelective(GpHisInfo gpHisInfo);

    GpHisInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GpHisInfo gpHisInfo);

    int updateByPrimaryKey(GpHisInfo gpHisInfo);

    List<GpHisInfo> selectByParams(Map<String, Object> map);
}