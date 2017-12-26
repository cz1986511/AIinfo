package com.danlu.dleye.persist.mapper;

import java.util.List;
import java.util.Map;

import com.danlu.dleye.persist.base.AddressInfo;

public interface AddressInfoMapper
{
    int deleteByPrimaryKey(Long id);

    int deleteAdressInfo(AddressInfo addressInfo);

    int insert(AddressInfo addressInfo);

    int insertSelective(AddressInfo addressInfo);

    AddressInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AddressInfo addressInfo);

    int updateByPrimaryKey(AddressInfo addressInfo);

    List<AddressInfo> selectByParams(Map<String, Object> map);
}