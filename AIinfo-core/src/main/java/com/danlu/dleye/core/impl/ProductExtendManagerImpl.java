package com.danlu.dleye.core.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.danlu.dleye.core.ProductExtendManager;
import com.danlu.dleye.persist.base.ProductExtend;
import com.danlu.dleye.persist.mapper.ProductExtendMapper;

public class ProductExtendManagerImpl implements ProductExtendManager
{

    @Autowired
    private ProductExtendMapper productExtendMapper;

    @Override
    public ProductExtend getProductExtendByProductId(Long productId)
    {
        if (null != productId && productId > 0)
        {
            return productExtendMapper.selectByProductId(productId);
        }
        return null;
    }

}
