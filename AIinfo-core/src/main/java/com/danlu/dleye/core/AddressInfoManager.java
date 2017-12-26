package com.danlu.dleye.core;

import java.util.List;
import java.util.Map;

import com.danlu.dleye.persist.base.AddressInfo;

public interface AddressInfoManager
{
    List<AddressInfo> getAddressInfoByParams(Map<String, Object> map);

    int addNewAddress(AddressInfo addressInfo);

    int updateAddress(AddressInfo addressInfo);

    int deleteAddress(AddressInfo addressInfo);

}
