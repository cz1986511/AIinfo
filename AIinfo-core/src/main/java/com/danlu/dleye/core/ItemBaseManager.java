package com.danlu.dleye.core;

import java.util.List;
import java.util.Map;

import com.danlu.dleye.persist.base.ItemBase;

/**
 * @author chenzhuo@danlu.com
 * @date 2017-09-01 11:23
 */
public interface ItemBaseManager
{

    /**
     * @param: itemIds: 商品id列表(list) itemName: 商品名称(支持模糊查询) itemCategoryCode:
     *         商品类目code itemBrandCode: 商品品牌code itemShopIds: 商品所属店铺id列表(list)
     *         itemStatus: 商品状态(01上架 02下架 99删除) itemType: 商品类型
     * @return:List<Item>
     * 
     */
    List<ItemBase> getItemBasesByParams(Map<String, Object> map);

    /**
     * @param: itemId 商品Id
     * @return:ItemBase
     * 
     */
    ItemBase getItemBaseById(Long itemId);

}
