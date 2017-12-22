package com.danlu.dleye.persist.mapper;

import com.danlu.dleye.persist.base.ProductBase;

public interface ProductBaseMapper
{
    int deleteByPrimaryKey(Long productId);

    int insert(ProductBase record);

    int insertSelective(ProductBase record);

    ProductBase selectByPrimaryKey(Long productId);

    int updateByPrimaryKeySelective(ProductBase record);

    int updateByPrimaryKey(ProductBase record);
}