package com.danlu.dleye.persist.mapper;

import com.danlu.dleye.persist.base.ProductExtend;

public interface ProductExtendMapper
{
    int deleteByPrimaryKey(Long productExtendId);

    int insert(ProductExtend productExtend);

    int insertSelective(ProductExtend productExtend);

    ProductExtend selectByPrimaryKey(Long productExtendId);

    ProductExtend selectByProductId(Long productId);

    int updateByPrimaryKeySelective(ProductExtend productExtend);

    int updateByPrimaryKeyWithBLOBs(ProductExtend productExtend);

    int updateByPrimaryKey(ProductExtend productExtend);
}