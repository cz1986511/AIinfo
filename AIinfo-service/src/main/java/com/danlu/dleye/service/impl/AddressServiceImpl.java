package com.danlu.dleye.service.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.danlu.dleye.core.AddressInfoManager;
import com.danlu.dleye.persist.base.AddressInfo;
import com.danlu.dleye.service.AddressService;

public class AddressServiceImpl implements AddressService
{
    private static final Logger log = LoggerFactory.getLogger(AddressServiceImpl.class);

    @Autowired
    private AddressInfoManager addressInfoManager;

    @Override
    public List<AddressInfo> getAddressInfos(Map<String, Object> map)
    {
        try
        {
            if (!CollectionUtils.isEmpty(map))
            {
                return addressInfoManager.getAddressInfoByParams(map);
            }
        }
        catch (Exception e)
        {
            log.error("getAddressInfos is exception:" + e.toString());
        }
        return null;
    }

    @Override
    public int addNewAddress(AddressInfo addressInfo)
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int updateAddress(AddressInfo addressInfo)
    {
        // TODO Auto-generated method stub
        return 0;
    }

}
