package com.danlu.dleye.core.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.danlu.dleye.client.entity.FudaiDetail;
import com.danlu.dleye.client.entity.FudaiItemInfo;
import com.danlu.dleye.client.entity.FudaiPictureInfo;
import com.danlu.dleye.constants.FuDaiConstants;
import com.danlu.dleye.core.FudaiManager;
import com.danlu.dleye.persist.base.FudaiInfo;
import com.danlu.dleye.persist.base.FudaiItem;
import com.danlu.dleye.persist.base.FudaiPicture;
import com.danlu.dleye.persist.base.FudaiSubscribe;
import com.danlu.dleye.persist.mapper.FudaiInfoMapper;
import com.danlu.dleye.persist.mapper.FudaiItemMapper;
import com.danlu.dleye.persist.mapper.FudaiPictureMapper;
import com.danlu.dleye.persist.mapper.FudaiSubscribeMapper;
import com.danlu.dleye.util.DlBeanUtils;
import com.danlu.dleye.util.IDUtils;

public class FudaiManagerImpl implements FudaiManager
{
    @Autowired
    private FudaiInfoMapper fudaiInfoMapper;
    @Autowired
    private FudaiItemMapper fudaiItemMapper;
    @Autowired
    private FudaiPictureMapper fudaiPictureMapper;
    @Autowired
    private FudaiSubscribeMapper fudaiSubscribeMapper;

    @Override
    public int addFudai(FudaiDetail fudaiDetail)
    {
        int result = 0;
        if (null != fudaiDetail)
        {
            FudaiInfo fudaiInfo = new FudaiInfo();
            DlBeanUtils.copyProperties(fudaiDetail, fudaiInfo);
            fudaiInfo.setFdId(IDUtils.nextUUID());
            result = fudaiInfoMapper.insertSelective(fudaiInfo);
            List<FudaiItemInfo> fudaiItemInfos = fudaiDetail.getFudaiItemInfos();
            if (!CollectionUtils.isEmpty(fudaiItemInfos))
            {
                Iterator<FudaiItemInfo> ite1 = fudaiItemInfos.iterator();
                while (ite1.hasNext())
                {
                    FudaiItemInfo temp = ite1.next();
                    FudaiItem fudaiItem = new FudaiItem();
                    DlBeanUtils.copyProperties(temp, fudaiItem);
                    fudaiItem.setFdId(fudaiInfo.getFdId());
                    fudaiItemMapper.insertSelective(fudaiItem);
                }
            }
            List<FudaiPictureInfo> fudaiPictureInfos = fudaiDetail.getFudaiPictureInfos();
            if (!CollectionUtils.isEmpty(fudaiPictureInfos))
            {
                Iterator<FudaiPictureInfo> ite2 = fudaiPictureInfos.iterator();
                while (ite2.hasNext())
                {
                    FudaiPictureInfo temp = ite2.next();
                    FudaiPicture fudaiPicture = new FudaiPicture();
                    DlBeanUtils.copyProperties(temp, fudaiPicture);
                    fudaiPicture.setFdId(fudaiInfo.getFdId());
                    fudaiPictureMapper.insertSelective(fudaiPicture);
                }
            }
        }
        return result;
    }

    @Override
    public int updateFudai(FudaiDetail fudaiDetail)
    {
        int result = 0;
        if (null != fudaiDetail)
        {
            FudaiInfo fudaiInfo = new FudaiInfo();
            DlBeanUtils.copyProperties(fudaiDetail, fudaiInfo);
            result = fudaiInfoMapper.updateByPrimaryKeySelective(fudaiInfo);
        }
        return result;
    }

    @Override
    public List<FudaiDetail> getFudaiDetailsByParams(Map<String, Object> map)
    {
        if (!CollectionUtils.isEmpty(map))
        {
            List<FudaiInfo> fudaiInfos = fudaiInfoMapper.selectByParams(map);
            if (!CollectionUtils.isEmpty(fudaiInfos))
            {
                List<FudaiDetail> fudaiDetails = new ArrayList<FudaiDetail>();
                List<String> fdIds = new ArrayList<String>();
                Iterator<FudaiInfo> ite1 = fudaiInfos.iterator();
                while (ite1.hasNext())
                {
                    FudaiInfo temp = ite1.next();
                    FudaiDetail fudaiDetail = new FudaiDetail();
                    DlBeanUtils.copyProperties(temp, fudaiDetail);
                    fudaiDetails.add(fudaiDetail);
                    fdIds.add(temp.getFdId());
                }
                if (null != map.get("isDetail"))
                {
                    Map<String, Object> map2 = new HashMap<String, Object>();
                    map2.put("fdIds", fdIds);
                    List<FudaiItem> fudaiItems = fudaiItemMapper.selectByParams(map2);
                    Map<String, List<FudaiItemInfo>> map3 = new HashMap<String, List<FudaiItemInfo>>();
                    if (!CollectionUtils.isEmpty(fudaiItems))
                    {
                        Iterator<FudaiItem> ite2 = fudaiItems.iterator();
                        while (ite2.hasNext())
                        {
                            FudaiItem temp2 = ite2.next();
                            FudaiItemInfo fudaiItemInfo = new FudaiItemInfo();
                            DlBeanUtils.copyProperties(temp2, fudaiItemInfo);
                            List<FudaiItemInfo> fudaiItemInfos = map3.get(temp2.getFdId());
                            if (null != fudaiItemInfos)
                            {
                                fudaiItemInfos.add(fudaiItemInfo);
                            }
                            else
                            {
                                fudaiItemInfos = new ArrayList<FudaiItemInfo>();
                                fudaiItemInfos.add(fudaiItemInfo);
                                map3.put(temp2.getFdId(), fudaiItemInfos);
                            }
                        }
                    }

                    List<FudaiPicture> fudaiPictures = fudaiPictureMapper.selectByParams(map2);
                    Map<String, List<FudaiPictureInfo>> map4 = new HashMap<String, List<FudaiPictureInfo>>();
                    if (!CollectionUtils.isEmpty(fudaiPictures))
                    {
                        Iterator<FudaiPicture> ite3 = fudaiPictures.iterator();
                        while (ite3.hasNext())
                        {
                            FudaiPicture temp3 = ite3.next();
                            FudaiPictureInfo fudaiPictureInfo = new FudaiPictureInfo();
                            DlBeanUtils.copyProperties(temp3, fudaiPictureInfo);
                            List<FudaiPictureInfo> fudaiPictureInfos = map4.get(temp3.getFdId());
                            if (null != fudaiPictureInfos)
                            {
                                fudaiPictureInfos.add(fudaiPictureInfo);
                            }
                            else
                            {
                                fudaiPictureInfos = new ArrayList<FudaiPictureInfo>();
                                fudaiPictureInfos.add(fudaiPictureInfo);
                                map4.put(temp3.getFdId(), fudaiPictureInfos);
                            }
                        }
                    }
                    Iterator<FudaiDetail> iterator = fudaiDetails.iterator();
                    while (iterator.hasNext())
                    {
                        FudaiDetail temp = iterator.next();
                        temp.setFudaiItemInfos(map3.get(temp.getFdId()));
                        temp.setFudaiPictureInfos(map4.get(temp.getFdId()));
                    }
                }
                return fudaiDetails;
            }
        }
        return null;
    }

    @Override
    public int deleteFudai(Map<String, Object> map)
    {
        int result = 0;
        if (!CollectionUtils.isEmpty(map))
        {
            FudaiSubscribe fudaiSubscribe = new FudaiSubscribe();
            fudaiSubscribe.setFdId((String) map.get("fdId"));
            fudaiSubscribe.setUserId((Long) map.get("userId"));
            result = fudaiSubscribeMapper.deleteByFdIdAndUserId(fudaiSubscribe);
            Set<String> fdIds = new HashSet<String>();
            fdIds.add(fudaiSubscribe.getFdId());
            List<FudaiInfo> fudaiInfos = fudaiInfoMapper.selectByParams(map);
            if(!CollectionUtils.isEmpty(fudaiInfos)){
                FudaiDetail fudaiDetail = null;
                for (FudaiInfo info : fudaiInfos)
                {
                    if(info != null && info.getUserId() != null && info.getUserId().equals(fudaiSubscribe.getUserId())){
                        fudaiDetail = new FudaiDetail();
                        fudaiDetail.setFdId((String) map.get("fdId"));
                        fudaiDetail.setFdStatus(FuDaiConstants.STATUS.DELETE);
                        result +=  this.updateFudai(fudaiDetail);
                    }
                }
            }
        }
        return result;
    }

    @Override
    public int addFudaiSubscribe(FudaiSubscribe fudaiSubscribe)
    {
        if (null != fudaiSubscribe)
        {
            return fudaiSubscribeMapper.insertSelective(fudaiSubscribe);
        }
        return 0;
    }

    @Override
    public List<FudaiSubscribe> getFudaiSubscribes(Map<String, Object> map)
    {
        if (!CollectionUtils.isEmpty(map))
        {
            return fudaiSubscribeMapper.selectByParams(map);
        }
        return null;
    }
}
