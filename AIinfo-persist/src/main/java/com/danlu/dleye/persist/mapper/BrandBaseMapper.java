package com.danlu.dleye.persist.mapper;

import com.danlu.dleye.persist.base.BrandBase;

public interface BrandBaseMapper
{
    int deleteByPrimaryKey(Long brandId);

    int insert(BrandBase record);

    int insertSelective(BrandBase record);

    BrandBase selectByPrimaryKey(Long brandId);

    int updateByPrimaryKeySelective(BrandBase record);

    int updateByPrimaryKey(BrandBase record);
}