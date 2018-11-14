package xiaozhuo.info.service;

import xiaozhuo.info.persist.base.OilInfo;

public interface OilInfoService {

    int addOilInfo(OilInfo oilInfo);

    int updateOilInfo(OilInfo oilInfo);

    OilInfo getOilInfoById(Long id);

}
