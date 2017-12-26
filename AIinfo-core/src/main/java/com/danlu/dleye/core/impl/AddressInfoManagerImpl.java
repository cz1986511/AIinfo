package com.danlu.dleye.core.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.danlu.dleye.core.AddressInfoManager;
import com.danlu.dleye.persist.base.AddressInfo;
import com.danlu.dleye.persist.mapper.AddressInfoMapper;

public class AddressInfoManagerImpl implements AddressInfoManager
{
    @Autowired
    private AddressInfoMapper addressInfoMapper;

    @Override
    public List<AddressInfo> getAddressInfoByParams(Map<String, Object> map)
    {
        if (!CollectionUtils.isEmpty(map))
        {
            return addressInfoMapper.selectByParams(map);
        }
        return null;
    }

    @Override
    public int addNewAddress(AddressInfo addressInfo)
    {
        if (null != addressInfo)
        {
            return addressInfoMapper.insertSelective(addressInfo);
        }
        return 0;
    }

    @Override
    public int updateAddress(AddressInfo addressInfo)
    {
        if (null != addressInfo)
        {
            return addressInfoMapper.updateByPrimaryKeySelective(addressInfo);
        }
        return 0;
    }

    @Override
    public int deleteAddress(AddressInfo addressInfo)
    {
        if (null != addressInfo)
        {
            return addressInfoMapper.deleteAdressInfo(addressInfo);
        }
        return 0;
    }

}
