package com.danlu.dleye.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.danlu.dleye.client.entity.ItemInfo;
import com.danlu.dleye.core.ItemBaseManager;
import com.danlu.dleye.core.ProductExtendManager;
import com.danlu.dleye.core.util.RedisClient;
import com.danlu.dleye.persist.base.ItemBase;
import com.danlu.dleye.persist.base.ProductExtend;
import com.danlu.dleye.service.ItemService;
import com.danlu.dleye.service.SolrService;
import com.danlu.dleye.util.DlBeanUtils;

public class ItemServiceImpl implements ItemService
{

    private final static Logger log = LoggerFactory.getLogger(ItemServiceImpl.class);

    @Autowired
    private ItemBaseManager itemManager;
    @Autowired
    private ProductExtendManager productExtendManager;
    @Autowired
    private RedisClient redisClient;
    @Autowired
    private SolrService solrService;

    @Override
    public ItemInfo getItemById(Long itemId)
    {
        ItemInfo itemInfo = null;
        if (null != itemId && itemId > 0)
        {
            itemInfo = redisClient.get(String.valueOf(itemId), new TypeReference<ItemInfo>()
            {
            });
            if (null == itemInfo)
            {
                itemInfo = getItemInfo(itemId);
                if (null != itemInfo)
                {
                    redisClient.set(String.valueOf(itemId), itemInfo, 600);
                }
                else
                {
                    log.info("getItemById id is null|id:" + itemId);
                }
            }
        }
        return itemInfo;
    }

    @Override
    public List<ItemInfo> getItemsByParams(Map<String, Object> inputData)
    {
        if (!CollectionUtils.isEmpty(inputData))
        {
            try
            {
                List<ItemBase> itemBases = itemManager.getItemBasesByParams(inputData);
                if (!CollectionUtils.isEmpty(itemBases))
                {
                    List<ItemInfo> result = new ArrayList<ItemInfo>();
                    Iterator<ItemBase> ite1 = itemBases.iterator();
                    while (ite1.hasNext())
                    {
                        ItemBase temp = ite1.next();
                        ItemInfo itemInfo = new ItemInfo();
                        DlBeanUtils.copyProperties(temp, itemInfo);
                        result.add(itemInfo);
                    }
                    return result;
                }
            }
            catch (Exception e)
            {
                log.error("getItemsByParams is Exception:" + e.toString());
            }
        }

        return null;
    }

    private ItemInfo getItemInfo(Long itemId)
    {
        ItemInfo itemInfo = null;
        ProductExtend productExtend = null;
        try
        {
            ItemBase itemBase = itemManager.getItemBaseById(itemId);
            if (null != itemBase)
            {
                Long productId = itemBase.getItemProductId();
                itemInfo = new ItemInfo();
                DlBeanUtils.copyProperties(itemBase, itemInfo);
                productExtend = redisClient.get(String.valueOf(productId) + "-extend",
                    new TypeReference<ProductExtend>()
                    {
                    });
                if (null == productExtend)
                {
                    productExtend = productExtendManager.getProductExtendByProductId(productId);
                    if (null != productExtend)
                    {
                        redisClient.set(String.valueOf(productId) + "-extend", productExtend, 3600);
                    }
                }
                if (null != productExtend)
                {
                    itemInfo.setItemCoverPic(productExtend.getProductCoverPic());
                    itemInfo.setItemDetailPic(productExtend.getProductDetailPic());
                    itemInfo.setItemDetailInfo(productExtend.getProductDetailInfo());
                }
            }
        }
        catch (Exception e)
        {
            log.error("getItemInfo is Exception:" + e.getMessage());
        }
        return itemInfo;
    }

    @Override
    public JSONObject getItemsByParamsFromSolr(Map<String, Object> inputData)
    {
        return solrService.searchItemInfos(inputData);
    }

}
