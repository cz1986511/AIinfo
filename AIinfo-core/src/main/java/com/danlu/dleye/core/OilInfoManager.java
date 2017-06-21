package com.danlu.dleye.core;

import com.danlu.dleye.persist.base.OilInfo;

public interface OilInfoManager {

    int addOilInfo(OilInfo oilInfo);

    int updateOilInfo(OilInfo oilInfo);

    OilInfo getOilInfoById(Long id);

}
