package com.danlu.dleye.persist.mapper;

import com.danlu.dleye.persist.base.GpPrecInfo;

public interface GpPrecInfoMapper
{
    int deleteByPrimaryKey(Long id);

    int insert(GpPrecInfo gpPrecInfo);

    int insertSelective(GpPrecInfo gpPrecInfo);

    GpPrecInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GpPrecInfo gpPrecInfo);

    int updateByPrimaryKey(GpPrecInfo gpPrecInfo);
}