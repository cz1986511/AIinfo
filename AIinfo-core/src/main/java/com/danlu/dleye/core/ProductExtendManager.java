package com.danlu.dleye.core;

import com.danlu.dleye.persist.base.ProductExtend;

/**
 * @author chenzhuo@danlu.com
 * @date 2017-09-26 15:09
 * 
 */
public interface ProductExtendManager
{

    ProductExtend getProductExtendByProductId(Long productId);

}
