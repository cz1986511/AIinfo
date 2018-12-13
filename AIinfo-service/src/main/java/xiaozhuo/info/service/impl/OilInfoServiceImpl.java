package xiaozhuo.info.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xiaozhuo.info.persist.base.OilInfo;
import xiaozhuo.info.persist.mapper.OilInfoMapper;
import xiaozhuo.info.service.OilInfoService;

@Service
public class OilInfoServiceImpl implements OilInfoService {

	@Autowired
	private OilInfoMapper oilInfoMapper;

	@Override
	public int addOilInfo(OilInfo oilInfo) {
		if (null != oilInfo) {
			return oilInfoMapper.insertSelective(oilInfo);
		}
		return 0;
	}

	@Override
	public int updateOilInfo(OilInfo oilInfo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public OilInfo getOilInfoById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

}
