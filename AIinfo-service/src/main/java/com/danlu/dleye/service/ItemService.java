package com.danlu.dleye.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.danlu.dleye.client.entity.ItemInfo;

/**
 * @author chenzhuo@danlu.com
 * @date 2017-09-05 13:40
 */
public interface ItemService
{

    /**
     * @param:itemId
     * @return:ItemBase
     */
    ItemInfo getItemById(Long itemId);

    /**
     * @param: shopFilter: 是否过滤店铺 itemIds: 商品id列表(list) itemName: 商品名称(支持模糊查询)
     *         itemCategoryCode: 商品类目code itemBrandCode: 商品品牌code itemShopIds:
     *         商品所属店铺id列表(list) itemStatus: 商品状态(01上架 02下架 99删除) itemType: 商品类型
     * @return:List<Item>
     * 
     */
    List<ItemInfo> getItemsByParams(Map<String, Object> inputData);

    /**
     * 
     * 
     */
    JSONObject getItemsByParamsFromSolr(Map<String, Object> inputData);

}
